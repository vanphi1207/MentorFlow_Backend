package vn.ihqqq.MentorFlow.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.enums.Gender;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String userId;
    String username;
    String firstName;
    String lastName;
    String email;
    String password;
    LocalDate birthday;
    Gender gender;
    String genderDisplay;
}
