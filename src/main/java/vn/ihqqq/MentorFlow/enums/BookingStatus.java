package vn.ihqqq.MentorFlow.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BookingStatus {
    PENDING("Đang chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    COMPLETED("Đã hoàn thành"),
    CANCELLED("Đã hủy");

    String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }
}

