package vn.ihqqq.MentorFlow.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.ihqqq.MentorFlow.enums.BookingStatus;

public class BookingStatusValidator implements ConstraintValidator<ValidBookingStatus, BookingStatus> {
    @Override
    public boolean isValid(BookingStatus value, ConstraintValidatorContext context) {
        for (BookingStatus status : BookingStatus.values()) {
            if (value == status) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void initialize(ValidBookingStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
