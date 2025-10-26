package vn.ihqqq.MentorFlow.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 3, message = "USERNAME_INVALID")
    String username;
    String firstName;
    String lastName;

    String email;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    LocalDate birthday;
    String gender;
}
