package vn.ihqqq.MentorFlow.dto.response.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookAvailabilityResponse {
    String bookAvailabilityId;
    String userId;
    String mentorId;
    String fullName;
    String username;
    LocalDate date;
    boolean isBooked;

    ScheduleSlotResponse slot;
}
