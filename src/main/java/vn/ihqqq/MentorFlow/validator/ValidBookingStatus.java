package vn.ihqqq.MentorFlow.validator;

import jakarta.validation.Payload;

public @interface ValidBookingStatus {
    String message() default "INVALID_BOOKING_STATUS";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
