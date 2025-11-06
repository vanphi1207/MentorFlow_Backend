package vn.ihqqq.MentorFlow.dto.request.booking;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleSlotRequest {

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    LocalTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    LocalTime endTime;

}
