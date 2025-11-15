package vn.ihqqq.MentorFlow.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PaymentMethod {
    VNPAY("VNPay"),
    CASH("Tiền mặt"),
    BANK_TRANSFER("Chuyển khoản");

    String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
}