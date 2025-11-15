package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.ihqqq.MentorFlow.entity.booking.ScheduleSlot;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, String> {

    // Kiểm tra slot đã tồn tại chưa
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM ScheduleSlot s WHERE s.startTime = :startTime AND s.endTime = :endTime AND s.dayOfWeek = :dayOfWeek")
    boolean existsByStartTimeAndEndTimeAndDayOfWeek(
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("dayOfWeek") DayOfWeek dayOfWeek
    );

    // Tìm slot theo thời gian
    Optional<ScheduleSlot> findByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);

}
