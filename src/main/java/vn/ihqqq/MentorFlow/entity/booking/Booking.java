package vn.ihqqq.MentorFlow.entity.booking;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    @JoinColumn(name = "userId")
    User user;

    Date time;
    String topic;
    String connectionForm;
    Date dateBook;
    String status;
    String note;
}
