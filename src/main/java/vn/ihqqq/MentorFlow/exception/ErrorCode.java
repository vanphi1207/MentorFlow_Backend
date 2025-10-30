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



    // Course
    TITTLE_COURSE_EXISTED(2001, "Title course existed", HttpStatus.CONFLICT),
    COURSE_NOT_FOUND(2002, "Course not found", HttpStatus.NOT_FOUND),
    FILE_TOO_LARGE(2003, "Kích thước file vượt quá giới hạn cho phép", HttpStatus.CONFLICT),
    INVALID_FILE_TYPE(2004, "Định dạng file không được hỗ trợ", HttpStatus.CONFLICT),
    UPLOAD_THUMBNAIL_FAILED(2005, "Upload thumbnail thất bại", HttpStatus.CONFLICT),
    UPLOAD_VIDEO_FAILED(2006, "Upload video thất bại" , HttpStatus.CONFLICT),
    FILE_EMPTY(2007, "File không được để trống", HttpStatus.CONFLICT),;
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
