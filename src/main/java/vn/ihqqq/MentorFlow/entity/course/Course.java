package vn.ihqqq.MentorFlow.entity.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.entity.user.MentorRequest;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.entity.user.UserCourse;
import vn.ihqqq.MentorFlow.enums.Level;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "course")
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String courseId;

    String titleCourse;
    String description;
    BigDecimal priceCourse;
    int timeCourse;
    @Enumerated(EnumType.STRING)
    Level level;
    int enrolledCount;
    String videoDemo;
    String thumbnailImg;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<CourseModule> modules = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<UserCourse> userCourses = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
