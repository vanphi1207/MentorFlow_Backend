package vn.ihqqq.MentorFlow.dto.response.Mentor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MentorRequestResponse {
    String Id;
    String name;
    String avatar;
    String linkMeet;
    BigDecimal priceBooking;
    String companyName;
    String position;
    String field;
    String softSkills;
    String status;
}
