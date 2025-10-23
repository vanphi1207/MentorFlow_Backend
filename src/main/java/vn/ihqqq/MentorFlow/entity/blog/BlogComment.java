package vn.ihqqq.MentorFlow.entity.blog;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.entity.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "blog_comment")
@Entity
public class BlogComment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime time;
    private String content;
    private int countLike;
}
