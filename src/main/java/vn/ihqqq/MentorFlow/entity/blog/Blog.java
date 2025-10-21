package vn.ihqqq.MentorFlow.entity.blog;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "blog")
@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String blogId;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

    LocalDateTime time;
    String content;
    String imgOrVideo;
    int countLike;
    int countComment;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    List<BlogComment> comments = new ArrayList<>();
}
