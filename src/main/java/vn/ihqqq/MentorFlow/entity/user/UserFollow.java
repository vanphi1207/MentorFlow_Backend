package vn.ihqqq.MentorFlow.entity.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_follow")
public class UserFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String followId;

    @ManyToOne
    @JoinColumn(name = "followerId")
    User follower;

    @ManyToOne
    @JoinColumn(name = "followingId")
    User following;
}
