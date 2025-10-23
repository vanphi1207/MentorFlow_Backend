package vn.ihqqq.MentorFlow.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_skill")
@Entity
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String skillId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String nameSkill;
    String description;
}
