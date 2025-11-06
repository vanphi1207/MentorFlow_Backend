package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.booking.BookAvailability;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookAvailabilityRepository extends JpaRepository<BookAvailability, String> {
    // Tìm availability theo mentor
    List<BookAvailability> findByUser_UserId(String userId);

    // Tìm availability theo mentor và ngày
    List<BookAvailability> findByUser_UserIdAndDate(String userId, LocalDate date);

    // Tìm availability theo ngày và slot, không bị book
    @Query("SELECT ba FROM BookAvailability ba WHERE ba.user.userId = :userId " +
            "AND ba.date = :date AND ba.slot.slotId = :slotId")
    Optional<BookAvailability> findByUserAndDateAndSlot(
            @Param("userId") String userId,
            @Param("date") LocalDate date,
            @Param("slotId") String slotId
    );

    // Tìm các slot available (chưa bị book) của mentor trong khoảng thời gian
    @Query("SELECT ba FROM BookAvailability ba WHERE ba.user.userId = :userId " +
            "AND ba.date BETWEEN :startDate AND :endDate " +
            "AND ba.booking IS NULL")
    List<BookAvailability> findAvailableSlotsByUserAndDateRange(
            @Param("userId") String userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Check xem slot đã bị book chưa
    @Query("SELECT CASE WHEN COUNT(ba) > 0 THEN true ELSE false END " +
            "FROM BookAvailability ba WHERE ba.bookAvailabilityId = :id AND ba.booking IS NOT NULL")
    boolean isSlotBooked(@Param("id") String id);
}
