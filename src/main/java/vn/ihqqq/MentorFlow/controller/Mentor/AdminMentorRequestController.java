package vn.ihqqq.MentorFlow.controller.Mentor;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.Mentor.MentorRequestResponse;
import vn.ihqqq.MentorFlow.enums.RequestStatus;
import vn.ihqqq.MentorFlow.service.Mentor.MentorRequestService;

import java.util.List;

@RestController
@RequestMapping("/admin/mentor-requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminMentorRequestController {

    MentorRequestService mentorRequestService;

    @PutMapping("/{id}/approve")
    public ApiResponse<MentorRequestResponse> approveRequest(@PathVariable String id) {
        return ApiResponse.<MentorRequestResponse>builder()
                .result(mentorRequestService.approveRequest(id))
                .build();
    }

    @PutMapping("/{id}/reject")
    public ApiResponse<MentorRequestResponse> rejectRequest(@PathVariable String id) {
        return ApiResponse.<MentorRequestResponse>builder()
                .result(mentorRequestService.rejectRequest(id))
                .build();
    }



    @GetMapping
    public ApiResponse<List<MentorRequestResponse>> getAllRequests(RequestStatus status) {
        return ApiResponse.<List<MentorRequestResponse>>builder()
                .result(mentorRequestService.getAllRequests(status))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRequest(@PathVariable String id) {
        mentorRequestService.deleteRequest(id);
        return ApiResponse.<String>builder()
                .result("Mentor deleted")
                .build();
    }

}
