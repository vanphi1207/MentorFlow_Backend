package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.booking.Booking;
import vn.ihqqq.MentorFlow.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    // Tìm booking theo user
    List<Booking> findByUser_UserId(String userId);

    // Tìm booking theo status (sử dụng Enum)
    List<Booking> findByStatus(BookingStatus status);

    // Tìm booking theo user và status
    List<Booking> findByUser_UserIdAndStatus(String userId, BookingStatus status);

    // Tìm booking theo ngày
    List<Booking> findByDateBook(LocalDate date);

    List<Booking> findByUser_UserIdAndDateBook(String userId, LocalDate date);

    // Tìm booking của mentor trong khoảng thời gian
    @Query("SELECT b FROM Booking b WHERE b.bookAvailability.user.userId = :mentorId " +
            "AND b.dateBook BETWEEN :startDate AND :endDate")
    List<Booking> findByMentorAndDateRange(
            @Param("mentorId") String mentorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.userId = :userId " +
            "AND b.status IN ('PENDING', 'CONFIRMED')")
    long countActiveBookingsByUserId(@Param("userId") String userId);

    @Query("SELECT b FROM Booking b WHERE b.dateBook = :date " +
            "AND b.status = 'CONFIRMED' " +
            "ORDER BY b.time ASC")
    List<Booking> findUpcomingBookingsByDate(@Param("date") LocalDate date);


}
