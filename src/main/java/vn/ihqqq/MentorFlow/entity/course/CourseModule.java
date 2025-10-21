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
@Table(name = "course_module")
public class CourseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String moduleId;

    @ManyToOne
    @JoinColumn(name = "courseId")
    Course course;

    String nameModule;
    String description;
    int timeModule;

    @OneToMany(mappedBy = "moduleCourse", cascade = CascadeType.ALL)
    List<Lesson> lessons = new ArrayList<>();
}
