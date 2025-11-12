package vn.ihqqq.MentorFlow.dto.response.Mentor;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String companyName;
    String position;
    String field;
    String softSkills;
    String status;
}
