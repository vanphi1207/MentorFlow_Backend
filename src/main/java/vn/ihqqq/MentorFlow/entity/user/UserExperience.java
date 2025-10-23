package vn.ihqqq.MentorFlow.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_experience")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class UserExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String experienceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    String nameCompany;
    String position;
    String description;
    LocalDate startDate;
    LocalDate endDate;
    String logo;

}
