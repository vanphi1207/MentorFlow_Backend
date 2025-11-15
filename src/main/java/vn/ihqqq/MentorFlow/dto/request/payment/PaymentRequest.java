package vn.ihqqq.MentorFlow.dto.request.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {

    @NotBlank(message = "Course ID không được để trống")
    String courseId;

    @NotNull(message = "Số tiền không được để trống")
    BigDecimal amount;

    String orderInfo;
    String bankCode; // Mã ngân hàng (optional)
}