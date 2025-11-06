package vn.ihqqq.MentorFlow.dto.request.booking;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.enums.BookingStatus;
import vn.ihqqq.MentorFlow.validator.ValidBookingStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingUpdateRequest {
    @Size(min = 5, max = 200, message = "Chủ đề phải từ 5-200 ký tự")
    String topic;

    @Size(max = 100, message = "Hình thức kết nối không quá 100 ký tự")
    String connectionForm;

    @Size(max = 500, message = "Ghi chú không quá 500 ký tự")
    String note;

    @ValidBookingStatus
    BookingStatus status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
}
