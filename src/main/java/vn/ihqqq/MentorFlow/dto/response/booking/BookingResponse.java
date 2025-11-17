package vn.ihqqq.MentorFlow.dto.response.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.enums.BookingStatus;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    String bookingId;
    LocalTime time;
    String topic;
    String connectionForm;
    BookingStatus status;
    String statusDisplay;
    String note;

    BookAvailabilityResponse bookAvailability;

}
