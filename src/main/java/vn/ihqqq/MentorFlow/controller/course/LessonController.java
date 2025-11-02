package vn.ihqqq.MentorFlow.controller.course;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.ihqqq.MentorFlow.dto.request.course.LessonCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.LessonUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.course.LessonResponse;
import vn.ihqqq.MentorFlow.service.course.LessonService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonController {
    LessonService lessonService;

    @PostMapping
    public ApiResponse<LessonResponse> createLesson(
            @RequestPart("data") @Valid LessonCreationRequest request,
            @RequestPart(value = "videoUrl", required = false) MultipartFile videoUrl) throws IOException {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.createLesson(request, videoUrl))
                .build();
    }

    @GetMapping("/{lessonId}")
    public ApiResponse<LessonResponse> getLessonById(@PathVariable("lessonId") String lessonId) {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.getLessonById(lessonId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<LessonResponse>> getLessons() {
        return ApiResponse.<List<LessonResponse>>builder()
                .result(lessonService.getAllLessons())
                .build();
    }

    @PutMapping("/{lessonId}")
    public ApiResponse<LessonResponse> updateLesson(
            @PathVariable("lessonId") String lessonId,
            @RequestPart("data") @Valid LessonUpdateRequest request,
            @RequestPart(value = "videoUrl", required = false) MultipartFile videoUrl) throws IOException {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.updateLesson(request, lessonId, videoUrl))
                .build();
    }

    @DeleteMapping("/{lessonId}")
    public ApiResponse<String> deleteLesson(@PathVariable("lessonId") String lessonId) {
        lessonService.deleteLesson(lessonId);

        return ApiResponse.<String>builder()
                .result("Lesson deleted")
                .build();
    }

}
