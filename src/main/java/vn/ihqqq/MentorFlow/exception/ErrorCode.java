package vn.ihqqq.MentorFlow.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 character", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters ", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    GENDER_INVALID(1007, "Gender must be MALE, FEMALE, OTHER, or PREFER_NOT_TO_SAY", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND(1009, "Role not found", HttpStatus.NOT_FOUND),



    // Course
    TITTLE_COURSE_EXISTED(2001, "Title course existed", HttpStatus.CONFLICT),
    NAME_MODULE_EXISTED(2002, "Name module existed", HttpStatus.CONFLICT),
    COURSE_EXISTED(2003, "Course existed", HttpStatus.CONFLICT),
    MODULE_NOT_FOUND(2004, "Module not found", HttpStatus.NOT_FOUND),
    COURSE_NOT_FOUND(2005, "Course not found", HttpStatus.NOT_FOUND),

    TITLE_LESSON_EXISTED(2006, "Title lesson existed", HttpStatus.CONFLICT),
    LESSON_NOT_FOUND(2007, "Lesson does not exist", HttpStatus.NOT_FOUND),

    // Booking & Schedule
    SLOT_NOT_FOUND(3001, "Schedule slot not found", HttpStatus.NOT_FOUND),
    SLOT_ALREADY_EXISTS(3002, "Schedule slot already exists", HttpStatus.CONFLICT),
    AVAILABILITY_NOT_FOUND(3003, "Book availability not found", HttpStatus.NOT_FOUND),
    SLOT_ALREADY_BOOKED(3004, "This time slot has already been booked", HttpStatus.CONFLICT),
    BOOKING_NOT_FOUND(3005, "Booking not found", HttpStatus.NOT_FOUND),
    CANNOT_BOOK_OWN_SLOT(3006, "You cannot book your own availability slot", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL_BOOKING(3007, "Cannot cancel this booking", HttpStatus.BAD_REQUEST),
    SLOT_TIME_INVALID(3008, "Start time must be before end time", HttpStatus.BAD_REQUEST),
    SLOT_DURATION_INVALID(3009, "Slot duration must be least 30 minutes", HttpStatus.BAD_REQUEST),
    AVAILABILITY_PAST_DATE(3010, "Cannot create availability for past dates", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_BOOKING_ACCESS(3011, "You don't have permission to access this booking", HttpStatus.FORBIDDEN),
    BOOKING_TIME_CONFLICT(3012, "You already booking at this time", HttpStatus.BAD_REQUEST),
    BOOKING_PAST_DATE(3013, "Cannot book a slot in the past", HttpStatus.BAD_REQUEST),
    BOOKING_TOO_CLOSE(3014, "Booking must be at least 2 hours in advance", HttpStatus.BAD_REQUEST),
    MAX_BOOKINGS_REACHED(3015, "You have reached the maximum number of bookings", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_PENDING(3016, "This booking is not pending", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_CONFIRMED(3017, "This booking is not confirmed", HttpStatus.BAD_REQUEST),


    // Blog
    BLOG_NOT_FOUND(2008, "Blog not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(2009, "Comment not found", HttpStatus.NOT_FOUND),
    BLOG_DONT_LIKE(2010, "Blog don't like this", HttpStatus.BAD_REQUEST),
    BLOG_NOT_LIKE(2011, "blog not liked", HttpStatus.BAD_REQUEST),


    //MENTOR_REQUEST
    REQUEST_NOT_FOUND(4001, "request not found", HttpStatus.BAD_REQUEST),
    REQUEST_BAD_REQUEST(4002, "request bad request", HttpStatus.BAD_REQUEST),
    SUBMITTED_REQUEST(4003, "You already submitted a mentor request", HttpStatus.BAD_REQUEST),


    // Payment
    PAYMENT_NOT_FOUND(5001, "Payment not found", HttpStatus.NOT_FOUND),
    PAYMENT_ALREADY_EXISTS(5002, "Payment already exists for this course", HttpStatus.CONFLICT),
    PAYMENT_FAILED(5003, "Payment failed", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_SIGNATURE(5004, "Invalid payment signature", HttpStatus.BAD_REQUEST),
    COURSE_ALREADY_PURCHASED(5005, "You have already purchased this course", HttpStatus.CONFLICT),

    ;



    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    int code;
    String message;
    HttpStatusCode statusCode;
}
