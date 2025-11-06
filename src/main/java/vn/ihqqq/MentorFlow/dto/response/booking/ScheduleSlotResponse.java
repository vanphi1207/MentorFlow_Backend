package vn.ihqqq.MentorFlow.dto.response.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleSlotResponse {
    String slotId;
    LocalTime startTime;
    LocalTime endTime;
}
