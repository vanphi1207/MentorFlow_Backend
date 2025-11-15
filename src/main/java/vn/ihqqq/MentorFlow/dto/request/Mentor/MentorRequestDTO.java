package vn.ihqqq.MentorFlow.dto.request.Mentor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MentorRequestDTO {
    String linkMeet;
    BigDecimal priceBooking;
    String avatar;
    String companyName;
    String position;
    String field;
    String softSkills;
}
