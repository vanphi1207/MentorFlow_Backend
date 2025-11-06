package vn.ihqqq.MentorFlow.controller.booking;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.request.booking.ScheduleSlotRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.booking.ScheduleSlotResponse;
import vn.ihqqq.MentorFlow.service.booking.ScheduleSlotService;

import java.util.List;

@RestController
@RequestMapping("/schedule-slots")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleSlotController {
    ScheduleSlotService scheduleSlotService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_SLOT')")
    public ApiResponse<ScheduleSlotResponse> createSlot(
            @Valid @RequestBody ScheduleSlotRequest request) {
        return ApiResponse.<ScheduleSlotResponse>builder()
                .result(scheduleSlotService.createSlot(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ScheduleSlotResponse>> getAllSlots() {
        return ApiResponse.<List<ScheduleSlotResponse>>builder()
                .result(scheduleSlotService.getAllSlots())
                .build();
    }

    @GetMapping("/{slotId}")
    public ApiResponse<ScheduleSlotResponse> getSlotById(@PathVariable String slotId) {
        return ApiResponse.<ScheduleSlotResponse>builder()
                .result(scheduleSlotService.getSlotById(slotId))
                .build();
    }

    @DeleteMapping("/{slotId}")
    @PreAuthorize("hasAuthority('DELETE_SLOT')")
    public ApiResponse<String> deleteSlot(@PathVariable String slotId) {
        scheduleSlotService.deleteSlot(slotId);
        return ApiResponse.<String>builder()
                .result("Schedule slot deleted")
                .build();
    }
}
