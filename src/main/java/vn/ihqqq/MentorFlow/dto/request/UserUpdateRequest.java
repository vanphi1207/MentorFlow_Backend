package vn.ihqqq.MentorFlow.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.enums.Gender;
import vn.ihqqq.MentorFlow.validator.ValidGender;

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
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    LocalDate birthday;

    @ValidGender
    Gender gender;

}
