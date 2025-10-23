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
@Table(name = "user_follow")
@Entity
public class UserFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String followId;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    User following;
}
