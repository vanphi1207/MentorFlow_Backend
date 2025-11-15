package vn.ihqqq.MentorFlow.entity.booking;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "slot")
    private List<BookAvailability> bookAvailabilities = new ArrayList<>();
}
