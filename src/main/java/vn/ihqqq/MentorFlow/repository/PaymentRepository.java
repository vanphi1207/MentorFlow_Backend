package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.payment.Payment;
import vn.ihqqq.MentorFlow.enums.PaymentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByUser_UserId(String userId);

    List<Payment> findByUser_UserIdAndStatus(String userId, PaymentStatus status);

    Optional<Payment> findByTransactionNo(String transactionNo);

    List<Payment> findByCourse_CourseId(String courseId);

    boolean existsByUser_UserIdAndCourse_CourseIdAndStatus(
            String userId, String courseId, PaymentStatus status);
}