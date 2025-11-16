package vn.ihqqq.MentorFlow.controller.Mentor;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.ihqqq.MentorFlow.dto.request.Mentor.MentorRequestDTO;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.Mentor.MentorRequestResponse;
import vn.ihqqq.MentorFlow.service.Mentor.MentorRequestService;

import java.io.IOException;

@RestController
@RequestMapping("/mentor")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MentorController {

    MentorRequestService mentorRequestService;

    @PostMapping("/request/{userId}")
    public ApiResponse<MentorRequestResponse> createMentorRequest(@PathVariable String userId ,@RequestPart("data") @Valid MentorRequestDTO request,
                                                                  @RequestPart(value = "avatarFile", required = false)
                                                                  MultipartFile avatarFile) throws IOException {
        return ApiResponse.<MentorRequestResponse>builder()
                .result(mentorRequestService.createMentorRequest(userId ,request, avatarFile))
                .build();
    }

    @GetMapping("/{mentorId}")
    public ApiResponse<MentorRequestResponse> getMentorRequest(@PathVariable String mentorId){
        return ApiResponse.<MentorRequestResponse>builder()
                .result(mentorRequestService.getMentorById(mentorId))
                .build();
    }
    @GetMapping("/user/{id}")
    public ApiResponse<MentorRequestResponse> getRequestByUserId(@PathVariable String id) {
        return ApiResponse.<MentorRequestResponse>builder()
                .result(mentorRequestService.getMentorByUserId(id))
                .build();
    }
}
