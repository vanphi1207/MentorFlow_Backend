package vn.ihqqq.MentorFlow.entity.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.entity.user.UserCourse;

import java.math.BigDecimal;
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
    String videoDemo;
    String thumbnailImg;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<CourseModule> modules = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<UserCourse> userCourses = new ArrayList<>();
}
