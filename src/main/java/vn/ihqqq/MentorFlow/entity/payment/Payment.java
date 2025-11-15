package vn.ihqqq.MentorFlow.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.entity.course.Course;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.enums.PaymentMethod;
import vn.ihqqq.MentorFlow.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payment")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    String transactionNo; // Mã giao dịch từ VNPay
    String bankCode;
    String cardType;
    String orderInfo;
    String vnpayTransactionNo; // Mã giao dịch VNPay
    String vnpayResponseCode;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}