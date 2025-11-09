package vn.ihqqq.MentorFlow.service.Blog;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ihqqq.MentorFlow.dto.request.Blog.BlogCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.Blog.BlogUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.Blog.BlogResponse;
import vn.ihqqq.MentorFlow.entity.blog.Blog;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.BlogMapper;
import vn.ihqqq.MentorFlow.repository.BlogRepository;
import vn.ihqqq.MentorFlow.service.UserService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BlogService {

    BlogRepository blogRepository;
    BlogMapper blogMapper;
    Cloudinary cloudinary;
    UserService userService;

    public BlogResponse createBlog(BlogCreationRequest request,
                                   MultipartFile fileImg) throws IOException {

        User currentUser = userService.getCurrentUser(); //

        Blog blog = blogMapper.toBlog(request);
        blog.setUser(currentUser);

        if (fileImg != null && !fileImg.isEmpty()) {
            String img = uploadImg(fileImg);
            blog.setImg(img);
        }

        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.toBlogResponse(savedBlog);
    }

    public String uploadImg(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        String publicValue = generatePublicValue(file.getOriginalFilename());
        String extension = getFileName(file.getOriginalFilename())[1];
        File fileUpload = convert(file);
        log.info("fileUpload is: {}", fileUpload);
        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                "public_id", publicValue,
                "folder", "blog"
        ));

        cleanDisk(fileUpload);

        return cloudinary.url().generate(publicValue + "." + extension);

    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convertFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()),
                getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convertFile.toPath());

        }
        return convertFile;
    }

    public String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName){
        return originalName.split("\\.");
    }

    public void cleanDisk(File file){
        try {
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    public BlogResponse getBlogById(String id){

        Blog blog = blogRepository.findById(id).orElseThrow(
                ()-> new AppException(ErrorCode.BLOG_NOT_FOUND));

        return blogMapper.toBlogResponse(blog);
    }

    public List<BlogResponse> getALlBlog(){
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream().map(blogMapper::toBlogResponse).toList();
    }

    @Transactional
    public BlogResponse updateBlog(BlogUpdateRequest request,
                                   String id,
                                   MultipartFile fileImg) throws IOException {
        Blog blog = blogRepository.findById(id).orElseThrow(
                ()-> new AppException(ErrorCode.BLOG_NOT_FOUND));

        blogMapper.updateBlog(request, blog);

        if (fileImg != null && !fileImg.isEmpty()) {
            //Xóa ảnh cũ trên Cloudinary (nếu có)
            if (blog.getImg() != null) {
                deleteFromCloudinary(blog.getImg(), "image");
            }

            // Upload ảnh mới
            String thumbnailUrl = uploadImg(fileImg);
            blog.setImg(thumbnailUrl);
        }

        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.toBlogResponse(savedBlog);
    }

    public void deleteFromCloudinary(String fileUrl, String resourceType) {
        try {
            String publicId = extractPublicId(fileUrl);
            if (publicId == null) {
                log.warn("Không thể extract publicId từ URL: {}", fileUrl);
                return;
            }

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                    "resource_type", resourceType
            ));

            log.info("Deleted {} from Cloudinary: {} | Result: {}", resourceType, publicId, result);
        } catch (Exception e) {
            log.error("Failed to delete {} from Cloudinary: {}", resourceType, e.getMessage());
        }
    }


    private String extractPublicId(String url) {
        try {
            String[] parts = url.split("/");
            int uploadIndex = Arrays.asList(parts).indexOf("upload");
            if (uploadIndex == -1 || uploadIndex + 1 >= parts.length) {
                throw new IllegalArgumentException("Invalid Cloudinary URL: " + url);
            }

            String afterUpload = String.join("/",
                    Arrays.copyOfRange(parts, uploadIndex + 1, parts.length));

            // 🚨 XÓA phần thêm "blog/" cứng - CHỈ giữ phần này
            String publicId = afterUpload.substring(0, afterUpload.lastIndexOf('.'));

            log.info("Extracted publicId: {}", publicId);
            return publicId;

        } catch (Exception e) {
            log.error("Lỗi extract publicId từ URL {}: {}", url, e.getMessage());
            return null;
        }
    }

    public void deleteBlog(String id) {
        Blog blog = blogRepository.findById(id).orElseThrow(()->
                new AppException(ErrorCode.BLOG_NOT_FOUND));

        if (blog.getImg() != null) {
            deleteFromCloudinary(blog.getImg(), "image");
        }

        blogRepository.delete(blog);
    }

}
