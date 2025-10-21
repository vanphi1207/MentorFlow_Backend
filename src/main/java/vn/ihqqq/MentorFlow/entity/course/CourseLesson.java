package vn.ihqqq.MentorFlow.entity.course;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "course_module")
public class CourseLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String lessonId;

    @ManyToOne
    @JoinColumn(name = "moduleId")
    ModuleCourse moduleCourse;

    String lessonTitle;
    LocalTime timeLesson;
    String videoURL;
}
