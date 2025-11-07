package vn.ihqqq.MentorFlow.entity.booking;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.entity.user.User;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    ScheduleSlot slot;

    LocalDate date;

    @OneToOne(mappedBy = "bookAvailability", fetch = FetchType.LAZY)
    Booking booking;

    @Transient
    public boolean isBooked() {
        return booking != null;
    }
}
