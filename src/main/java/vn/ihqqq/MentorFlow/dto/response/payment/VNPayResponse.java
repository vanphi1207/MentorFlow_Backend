package vn.ihqqq.MentorFlow.dto.response.payment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayResponse {
    String paymentUrl;
    String message;
    boolean success;
}