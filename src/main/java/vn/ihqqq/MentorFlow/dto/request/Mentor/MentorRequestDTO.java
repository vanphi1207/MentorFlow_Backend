package vn.ihqqq.MentorFlow.dto.request.Mentor;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MentorRequestDTO {
    String linkMeet;
    String avatar;
    String companyName;
    String position;
    String field;
    String softSkills;
}
