package vn.ihqqq.MentorFlow.entity.booking;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "booking")
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    LocalTime time;
    String topic;
    String connectionForm;
    LocalDate dateBook;

    @Enumerated(EnumType.STRING)
    BookingStatus status;

    String note;

    @OneToOne
    @JoinColumn(name = "book_Availability_id", unique = true)
    BookAvailability bookAvailability;
}
