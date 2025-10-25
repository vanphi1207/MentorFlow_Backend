package vn.ihqqq.MentorFlow.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {


    String firstName;
    String lastName;

    String email;
    String password;
    LocalDate birthday;
    String gender;

}
