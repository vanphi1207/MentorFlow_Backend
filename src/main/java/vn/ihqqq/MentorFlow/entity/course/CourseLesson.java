package vn.ihqqq.MentorFlow.entity.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "course_lesson")
@Entity
public class CourseLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String lessonId;

    @ManyToOne
    @JoinColumn(name = "module_id")
    CourseModule courseModule;

    String lessonTitle;
    int timeLesson;
    String videoURL;
}
