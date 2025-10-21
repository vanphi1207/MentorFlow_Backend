package vn.ihqqq.MentorFlow.entity.course;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID courseId;

    String titleCourse;
    String description;
    float priceCourse;
    int timeCourse;
    String videoDemo;
    String thumbnailImg;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<ModuleCourse> modules = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<UserCourse> userCourses = new ArrayList<>();
}
