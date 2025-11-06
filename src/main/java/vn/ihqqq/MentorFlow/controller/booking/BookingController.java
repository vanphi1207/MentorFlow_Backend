package vn.ihqqq.MentorFlow.controller.booking;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.request.booking.BookingCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.booking.BookingUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.booking.BookingResponse;
import vn.ihqqq.MentorFlow.enums.BookingStatus;
import vn.ihqqq.MentorFlow.service.booking.BookingService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;

    @PostMapping
    public ApiResponse<BookingResponse> createBooking(
            @Valid @RequestBody BookingCreationRequest request) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.createBooking(request))
                .build();
    }

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponse> getBookingById(@PathVariable String bookingId) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.getBookingById(bookingId))
                .build();
    }

    @GetMapping("/my-bookings")
    public ApiResponse<List<BookingResponse>> getMyBookings() {
        return ApiResponse.<List<BookingResponse>>builder()
                .result(bookingService.getMyBookings())
                .build();
    }

    @GetMapping("/my-bookings/status/{status}")
    public ApiResponse<List<BookingResponse>> getMyBookingsByStatus(
            @PathVariable BookingStatus status) {
        return ApiResponse.<List<BookingResponse>>builder()
                .result(bookingService.getMyBookingsByStatus(status))
                .build();
    }

    @GetMapping("/mentor")
    public ApiResponse<List<BookingResponse>> getBookingsForMentor(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.<List<BookingResponse>>builder()
                .result(bookingService.getBookingsForMentor(startDate, endDate))
                .build();
    }

    @PutMapping("/{bookingId}")
    public ApiResponse<BookingResponse> updateBooking(
            @PathVariable String bookingId,
            @Valid @RequestBody BookingUpdateRequest request) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.updateBooking(bookingId, request))
                .build();
    }

    @PutMapping("/{bookingId}/confirm")
    public ApiResponse<BookingResponse> confirmBooking(@PathVariable String bookingId) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.confirmBooking(bookingId))
                .build();
    }

    @PutMapping("/{bookingId}/complete")
    public ApiResponse<BookingResponse> completeBooking(@PathVariable String bookingId) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.completeBooking(bookingId))
                .build();
    }

    @PutMapping("/{bookingId}/cancel")
    public ApiResponse<String> cancelBooking(@PathVariable String bookingId, String reason) {
        bookingService.cancelBooking(bookingId, reason);
        return ApiResponse.<String>builder()
                .result("Booking cancelled")
                .build();
    }
}
