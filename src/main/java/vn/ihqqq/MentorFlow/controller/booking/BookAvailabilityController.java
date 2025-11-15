package vn.ihqqq.MentorFlow.controller.booking;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.request.booking.BookAvailabilityRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.booking.BookAvailabilityResponse;
import vn.ihqqq.MentorFlow.service.booking.BookAvailabilityService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/availabilities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookAvailabilityController {
    BookAvailabilityService bookAvailabilityService;

    @PostMapping
    public ApiResponse<BookAvailabilityResponse> createAvailability(
            @Valid @RequestBody BookAvailabilityRequest request) {
        return ApiResponse.<BookAvailabilityResponse>builder()
                .result(bookAvailabilityService.createAvailability(request))
                .build();
    }

    @GetMapping("/my-availabilities")
    public ApiResponse<List<BookAvailabilityResponse>> getMyAvailabilities() {
        return ApiResponse.<List<BookAvailabilityResponse>>builder()
                .result(bookAvailabilityService.getMyAvailabilities())
                .build();
    }

    @GetMapping("/mentor/{userId}")
    public ApiResponse<List<BookAvailabilityResponse>> getAvailableSlotsByMentor(
            @PathVariable String userId) {
        return ApiResponse.<List<BookAvailabilityResponse>>builder()
                .result(bookAvailabilityService.getAvailableSlotsByMentor(userId))
                .build();
    }

    @GetMapping("/mentor/{mentorId}/date/{date}")
    public ApiResponse<List<BookAvailabilityResponse>> getAvailableSlotsByMentorAndDate(
            @PathVariable String mentorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.<List<BookAvailabilityResponse>>builder()
                .result(bookAvailabilityService.getAvailableSlotsByMentorAndDate(
                        mentorId, date))
                .build();
    }

    @DeleteMapping("/{availabilityId}")
    public ApiResponse<String> deleteAvailability(@PathVariable String availabilityId) {
        bookAvailabilityService.deleteAvailability(availabilityId);
        return ApiResponse.<String>builder()
                .result("Availability deleted")
                .build();
    }
}
