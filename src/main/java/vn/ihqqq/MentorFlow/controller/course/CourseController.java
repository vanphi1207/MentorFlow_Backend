package vn.ihqqq.MentorFlow.controller.course;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.ihqqq.MentorFlow.dto.request.course.CourseCreationRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.course.CourseResponse;
import vn.ihqqq.MentorFlow.service.course.CourseService;

import java.io.IOException;


@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {

    CourseService courseService;

    @PostMapping("/upload")
    public ApiResponse<CourseResponse> createCourse(
            @RequestPart("data") @Valid CourseCreationRequest request,
            @RequestPart(value = "fileImg", required = false) MultipartFile fileImg,
            @RequestPart(value = "fileVideo", required = false) MultipartFile fileVideo
    ) throws IOException {
        ApiResponse<CourseResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(courseService.createCourse(request, fileImg, fileVideo));
        return apiResponse;
    }
}

