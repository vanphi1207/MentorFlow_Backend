package vn.ihqqq.MentorFlow.entity.user;

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
@Table(name = "user_study")
@Entity
public class UserStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String studyID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String nameSchool;
    String specialized;
    String description;
    LocalDate startDate;
    LocalDate endDate;
    String logo;
}
