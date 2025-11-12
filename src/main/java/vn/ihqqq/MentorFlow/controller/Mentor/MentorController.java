package vn.ihqqq.MentorFlow.controller.Mentor;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.request.Mentor.MentorRequestDTO;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.Mentor.MentorRequestResponse;
import vn.ihqqq.MentorFlow.service.Mentor.MentorRequestService;

@RestController
@RequestMapping("/mentor")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MentorController {

    MentorRequestService mentorRequestService;

    @PostMapping("/request/{userId}")
    public ApiResponse<MentorRequestResponse> createMentorRequest(@PathVariable String userId,
                                                                  @RequestBody MentorRequestDTO request){
        return ApiResponse.<MentorRequestResponse>builder()
                .result(mentorRequestService.createMentorRequest(userId, request))
                .build();
    }
}
