package vn.ihqqq.MentorFlow.controller.course;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.ihqqq.MentorFlow.dto.request.course.CourseCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.CourseUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.course.CourseDetailsResponse;
import vn.ihqqq.MentorFlow.dto.response.course.CourseResponse;
import vn.ihqqq.MentorFlow.service.course.CourseService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {

    CourseService courseService;

    @PostMapping
//    @PreAuthorize("hasAuthority('CREATE_COURSE')")
    public ApiResponse<CourseResponse> createCourse(
            @RequestPart("data") @Valid CourseCreationRequest request,
            @RequestPart(value = "fileImg", required = false) MultipartFile fileImg,
            @RequestPart(value = "fileVideo", required = false) MultipartFile fileVideo,
            Authentication authentication
    ) throws IOException {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaimAsString("user_id");
        ApiResponse<CourseResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.createCourse(userId,request, fileImg, fileVideo));
        return apiResponse;
    }

    @GetMapping("/{courseId}")
    public ApiResponse<CourseResponse> getCourseById(@PathVariable String courseId) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.getCourseById(courseId))
                .build();
    }

    @GetMapping("/courseDetails/{courseId}")
    public ApiResponse<CourseDetailsResponse> getCourseDetails(@PathVariable String courseId) {
        return ApiResponse.<CourseDetailsResponse>builder()
                .result(courseService.getCourseDetails(courseId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CourseResponse>> getAllCourses() {
        return ApiResponse.<List<CourseResponse>>builder()
                .result(courseService.GetAllCourses())
                .build();
    }

    @PutMapping("/{courseId}")
    public ApiResponse<CourseResponse> updateCourse(@RequestPart("data") @Valid CourseUpdateRequest request,
                                                    @PathVariable String courseId,
                                                    @RequestPart(value = "fileImg", required = false) MultipartFile fileImg,
                                                    @RequestPart(value = "fileVideo", required = false) MultipartFile fileVideo)
            throws IOException {

        ApiResponse<CourseResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.updateCourse(request, courseId, fileImg, fileVideo));
        return apiResponse;
    }

    @DeleteMapping("/{courseId}")
    public ApiResponse<String> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return ApiResponse.<String>builder()
                .result("Course deleted")
                .build();
    }

    @GetMapping("/by-user/{userId}")
    public ApiResponse<List<CourseResponse>> getCourseByUser(@PathVariable String userId) {
        return ApiResponse.<List<CourseResponse>>builder()
                .result(courseService.getCoursesByUserId(userId))
                .build();
    }

}

