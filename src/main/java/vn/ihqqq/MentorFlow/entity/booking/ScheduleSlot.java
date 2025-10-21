package vn.ihqqq.MentorFlow.entity.booking;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "schedule_slot")
@Entity
public class ScheduleSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String slotId;

    LocalTime startTime;
    LocalTime endTime;
}
