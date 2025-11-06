package vn.ihqqq.MentorFlow.dto.request.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingCreationRequest {

    @NotBlank(message = "Booking availability ID không được để trống")
    String bookAvailabilityId;

    @NotBlank(message = "Chủ đề không được để trống")
    @Size(min = 5, max = 200, message = "Chủ đề phải từ 5-200 ký tự")
    String topic;

    @NotBlank(message = "Hình thức kết nối không được để trống")
    @Size(max = 100, message = "Hình thức kết nối không quá 100 ký tự")
    String connectionForm;

    @Size(max = 500, message = "Ghi chú không quá 500 ký tự")
    String note;
}
