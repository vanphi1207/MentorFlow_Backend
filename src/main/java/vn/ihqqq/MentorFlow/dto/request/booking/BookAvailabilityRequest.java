package vn.ihqqq.MentorFlow.dto.request.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookAvailabilityRequest {

    @NotBlank(message = "Slot ID không được để trống")
    String slotId;

    @NotNull(message = "Ngày không được để trống")
    @Future(message = "Ngày phải là ngày trong tương lai")
    LocalDate date;

}
