package vn.ihqqq.MentorFlow.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.ihqqq.MentorFlow.enums.Gender;

public class GenderValidator implements ConstraintValidator<ValidGender, Gender> {
    @Override
    public boolean isValid(Gender value, ConstraintValidatorContext context) {
        for(Gender gender : Gender.values()) {
            if(value == gender)
                return true;
        }

        return false;
    }

    @Override
    public void initialize(ValidGender constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
