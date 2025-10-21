package vn.ihqqq.MentorFlow.entity.booking;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "book_availability")
@Entity
public class BookAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String bookAvailabilityId;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

    @ManyToOne
    @JoinColumn(name = "slotId")
    ScheduleSlot slot;

    LocalDate date;
    boolean isBooked;
}
