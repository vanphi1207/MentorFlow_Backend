package vn.ihqqq.MentorFlow.service.course;

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
import vn.ihqqq.MentorFlow.dto.request.course.LessonCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.LessonUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.course.LessonResponse;
import vn.ihqqq.MentorFlow.entity.course.CourseLesson;
import vn.ihqqq.MentorFlow.entity.course.CourseModule;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.LessonMapper;
import vn.ihqqq.MentorFlow.repository.LessonRepository;
import vn.ihqqq.MentorFlow.repository.ModuleRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonService {
    LessonRepository lessonRepository;
    ModuleRepository moduleRepository;
    LessonMapper lessonMapper;
    Cloudinary cloudinary;

    @Transactional
    public LessonResponse createLesson(LessonCreationRequest request,
                                       MultipartFile fileVideo) throws IOException {

        if (lessonRepository.existsByLessonTitle(request.getLessonTitle())) {
            throw new AppException(ErrorCode.TITLE_LESSON_EXISTED);
        }

        CourseModule courseModule = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseLesson courseLesson = lessonMapper.toLesson(request);
        courseLesson.setCourseModule(courseModule);


        // Upload video
        if (fileVideo != null && !fileVideo.isEmpty()) {
            String videoUrl = uploadVideoDemo(fileVideo);
            courseLesson.setVideoURL(videoUrl);
        }

        // Lưu DB
        CourseLesson savedLesson = lessonRepository.save(courseLesson);
        return lessonMapper.toLessonResponse(savedLesson);
    }

    public String uploadVideoDemo(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;

        String publicValue = generatePublicValue(file.getOriginalFilename());
        String extension = getFileExtension(file.getOriginalFilename());

        // Tạo file tạm có đuôi .mp4
        File fileUpload = File.createTempFile(UUID.randomUUID().toString(), "." + extension);
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, fileUpload.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        //upload với resource_type = video
        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                "public_id", publicValue,
                "folder", "Course Lesson",
                "resource_type", "video"
        ));

        cleanDisk(fileUpload);

        //Trả về URL có phần mở rộng
        return cloudinary.url()
                .resourceType("video")
                .generate(publicValue + "." + extension);
    }

    // Hàm phụ
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
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



    // getLessonById
    public LessonResponse getLessonById(String id){
        CourseLesson courseLesson = lessonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        return lessonMapper.toLessonResponse(courseLesson);
    }

    public List<LessonResponse> getAllLessons(){
        List<CourseLesson> courseLessons = lessonRepository.findAll();
        return courseLessons
                .stream()
                .map(lessonMapper::toLessonResponse)
                .toList();
    }

    @Transactional
    public LessonResponse updateLesson(LessonUpdateRequest request,
                                       String id,
                                       MultipartFile fileVideo) throws IOException {
        CourseLesson courseLesson = lessonRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        lessonMapper.updateLessonFromRequest(request, courseLesson);

        // Nếu có video mới
        if (fileVideo != null && !fileVideo.isEmpty()) {
            // Xóa video cũ
            if (courseLesson.getVideoURL() != null) {
                deleteFromCloudinary(courseLesson.getVideoURL(), "video");
            }

            String videoUrl = uploadVideoDemo(fileVideo);
            courseLesson.setVideoURL(videoUrl);
        }

        CourseLesson savedLesson = lessonRepository.save(courseLesson);
        return lessonMapper.toLessonResponse(savedLesson);
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

            String afterUpload = String.join("/", Arrays.copyOfRange(parts, uploadIndex + 1, parts.length));

            if (!afterUpload.startsWith("Course Lesson/")) {
                afterUpload = "Course Lesson/" + afterUpload;
            }

            String publicId = afterUpload.substring(0, afterUpload.lastIndexOf('.'));

            log.info("Extracted publicId: {}", publicId);
            return publicId;
        } catch (Exception e) {
            log.error("Lỗi extract publicId từ URL {}: {}", url, e.getMessage());
            return null;
        }
    }

    @Transactional
    public void deleteLesson(String lessonId) {
        CourseLesson courseLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // Xóa video nếu có
        if (courseLesson.getVideoURL() != null) {
            deleteFromCloudinary(courseLesson.getVideoURL(), "video");
        }

        lessonRepository.deleteById(lessonId);
    }

}
