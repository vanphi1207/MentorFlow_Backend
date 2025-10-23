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
@Table(name = "user_info")
@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String infoId;


    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    String info;
}
