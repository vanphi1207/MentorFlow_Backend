package vn.ihqqq.MentorFlow.dto.response.payment;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.enums.PaymentMethod;
import vn.ihqqq.MentorFlow.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String paymentId;
    String userId;
    String courseId;
    String courseName;
    BigDecimal amount;
    PaymentMethod paymentMethod;
    PaymentStatus status;
    String statusDisplay;
    String transactionNo;
    String bankCode;
    String cardType;
    String orderInfo;
    String vnpayTransactionNo;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}