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
    import vn.ihqqq.MentorFlow.dto.request.course.CourseUpdateRequest;
    import vn.ihqqq.MentorFlow.dto.response.course.CourseDetailsResponse;
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
    import java.util.List;
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

            // Upload ảnh
            if (fileImg != null && !fileImg.isEmpty()) {
                String thumbnailUrl = uploadThumbnail(fileImg);
                course.setThumbnailImg(thumbnailUrl);
            }

            // Upload video
            if (fileVideo != null && !fileVideo.isEmpty()) {
                String videoUrl = uploadVideoDemo(fileVideo);
                course.setVideoDemo(videoUrl);
            }

            // Lưu DB
            Course savedCourse = courseRepository.save(course);
            return courseMapper.toCourseResponse(savedCourse);
        }



        public String uploadThumbnail(MultipartFile file) throws IOException {
            assert file.getOriginalFilename() != null;
            String publicValue = generatePublicValue(file.getOriginalFilename());
            String extension = getFileName(file.getOriginalFilename())[1];
            File fileUpload = convert(file);
            log.info("fileUpload is: {}", fileUpload);
            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                    "public_id", publicValue,
                    "folder", "course"
            ));

            cleanDisk(fileUpload);

            return cloudinary.url().generate(publicValue + "." + extension);

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
                    "folder", "course",
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


        public CourseResponse getCourseById(String id){
            Course course = courseRepository.findById(id)
                    .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

            return courseMapper.toCourseResponse(course);
        }

        public List<CourseResponse> GetAllCourses(){
            List<Course> courses = courseRepository.findAll();
            return courses.stream()
                    .map(courseMapper::toCourseResponse)
                    .toList();
        }

        @Transactional
        public CourseResponse updateCourse(CourseUpdateRequest request,
                                           String id,
                                           MultipartFile fileImg,
                                           MultipartFile fileVideo) throws IOException {
            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            // Cập nhật thông tin cơ bản
            courseMapper.updateCourse(request, course);

            if (fileImg != null && !fileImg.isEmpty()) {
                //Xóa ảnh cũ trên Cloudinary (nếu có)
                if (course.getThumbnailImg() != null) {
                    deleteFromCloudinary(course.getThumbnailImg(), "image");
                }

                // Upload ảnh mới
                String thumbnailUrl = uploadThumbnail(fileImg);
                course.setThumbnailImg(thumbnailUrl);
            }

            // Nếu có video mới
            if (fileVideo != null && !fileVideo.isEmpty()) {
                // Xóa video cũ
                if (course.getVideoDemo() != null) {
                    deleteFromCloudinary(course.getVideoDemo(), "video");
                }

                String videoUrl = uploadVideoDemo(fileVideo);
                course.setVideoDemo(videoUrl);
            }

            //  Cập nhật DB
            Course updatedCourse = courseRepository.save(course);
            return courseMapper.toCourseResponse(updatedCourse);

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

                if (!afterUpload.startsWith("course/")) {
                    afterUpload = "course/" + afterUpload;
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
        public void deleteCourse(String id) {
            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            // Xóa ảnh nếu có
            if (course.getThumbnailImg() != null) {
                deleteFromCloudinary(course.getThumbnailImg(), "image");
            }

            // Xóa video nếu có
            if (course.getVideoDemo() != null) {
                deleteFromCloudinary(course.getVideoDemo(), "video");
            }
            courseRepository.deleteById(id);
        }


        public CourseDetailsResponse getCourseDetails(String courseId) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            return CourseDetailsResponse.builder()
                    .courseId(course.getCourseId())
                    .titleCourse(course.getTitleCourse())
                    .description(course.getDescription())
                    .priceCourse(course.getPriceCourse())
                    .timeCourse(course.getTimeCourse())
                    .level(course.getLevel())
                    .enrolledCount(course.getEnrolledCount())
                    .thumbnailImg(course.getThumbnailImg())
                    .videoDemo(course.getVideoDemo())
                    .createdAt(course.getCreatedAt())
                    .updatedAt(course.getUpdatedAt())
                    .modules(course.getModules()
                            .stream()
                            .map(courseMapper::toModuleWithLessonResponse)
                            .toList())
                    .build();
        }


    }



