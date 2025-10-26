package vn.ihqqq.MentorFlow.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderValidator.class)
@Documented
public @interface ValidGender {
    String message() default "GENDER_INVALID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
