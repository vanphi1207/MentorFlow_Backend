    package vn.ihqqq.MentorFlow.service.course;

    import com.cloudinary.Cloudinary;
    import com.cloudinary.utils.ObjectUtils;
    import jakarta.transaction.Transactional;
    import lombok.AccessLevel;
    import lombok.RequiredArgsConstructor;
    import lombok.experimental.FieldDefaults;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;
    import vn.ihqqq.MentorFlow.dto.request.course.CourseCreationRequest;
    import vn.ihqqq.MentorFlow.dto.response.course.CourseResponse;
    import vn.ihqqq.MentorFlow.entity.course.Course;
    import vn.ihqqq.MentorFlow.entity.user.User;
    import vn.ihqqq.MentorFlow.enums.Level;
    import vn.ihqqq.MentorFlow.exception.AppException;
    import vn.ihqqq.MentorFlow.exception.ErrorCode;
    import vn.ihqqq.MentorFlow.mapper.CourseMapper;
    import vn.ihqqq.MentorFlow.repository.CourseRepository;

    import java.io.File;
    import java.io.IOException;
    import java.io.InputStream;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.StandardCopyOption;
    import java.time.LocalDateTime;
    import java.util.Arrays;
    import java.util.Map;
    import org.apache.commons.lang3.StringUtils;
    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public class CourseService {

        CourseMapper courseMapper;
        CourseRepository courseRepository;
        Cloudinary cloudinary;


        @Transactional
        public CourseResponse createCourse(CourseCreationRequest request,
                                           MultipartFile fileImg,
                                           MultipartFile fileVideo) throws IOException {

            if (courseRepository.existsByTitleCourse(request.getTitleCourse())) {
                throw new AppException(ErrorCode.TITTLE_COURSE_EXISTED);
            }

            Course course = courseMapper.toCourse(request);

            // ✅ Upload ảnh
            if (fileImg != null && !fileImg.isEmpty()) {
                String thumbnailUrl = uploadThumbnail(fileImg);
                course.setThumbnailImg(thumbnailUrl);
            }

            // ✅ Upload video
            if (fileVideo != null && !fileVideo.isEmpty()) {
                String videoUrl = uploadVideoDemo(fileVideo);
                course.setVideoDemo(videoUrl);
            }

            // ✅ Lưu DB
            Course savedCourse = courseRepository.save(course);
            return courseMapper.toCourseResponse(savedCourse);
        }



        public String uploadThumbnail(MultipartFile file) throws IOException {
            assert file.getOriginalFilename() != null;
            String publicValue = generatePublicValue(file.getOriginalFilename());
            String extension = getFileName(file.getOriginalFilename())[1];
            File fileUpload = convert(file);
            log.info("fileUpload is: {}", fileUpload);
            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));

            cleanDisk(fileUpload);

            return cloudinary.url().generate(publicValue + "." + extension);

        }
        public String uploadVideoDemo(MultipartFile file) throws IOException {
            assert file.getOriginalFilename() != null;

            String publicValue = generatePublicValue(file.getOriginalFilename());
            String extension = getFileExtension(file.getOriginalFilename());

            // ✅ Tạo file tạm có đuôi .mp4
            File fileUpload = File.createTempFile(UUID.randomUUID().toString(), "." + extension);
            try (InputStream is = file.getInputStream()) {
                Files.copy(is, fileUpload.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // ✅ Upload với resource_type = video
            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                    "public_id", publicValue,
                    "resource_type", "video"
            ));

            cleanDisk(fileUpload);

            // ✅ Trả về URL có phần mở rộng
            return cloudinary.url()
                    .resourceType("video")
                    .generate(publicValue + "." + extension);
        }

        // Hàm phụ
        private String getFileExtension(String filename) {
            return filename.substring(filename.lastIndexOf('.') + 1);
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


    }



