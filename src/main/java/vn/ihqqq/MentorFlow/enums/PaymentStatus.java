package vn.ihqqq.MentorFlow.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PaymentStatus {
    PENDING("Đang chờ thanh toán"),
    SUCCESS("Thanh toán thành công"),
    FAILED("Thanh toán thất bại"),
    CANCELLED("Đã hủy");

    String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
}