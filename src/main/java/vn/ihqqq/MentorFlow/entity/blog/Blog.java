package vn.ihqqq.MentorFlow.entity.blog;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "user_id")
    User user;

    LocalDateTime time;
    String content;
    String img;
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    List<BlogComment> comments = new ArrayList<>();
}
