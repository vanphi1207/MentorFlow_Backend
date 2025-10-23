package vn.ihqqq.MentorFlow.entity.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "course_module")
@Entity
public class CourseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String moduleId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;

    String nameModule;
    String description;
    int timeModule;

    @OneToMany(mappedBy = "courseModule", cascade = CascadeType.ALL)
    List<CourseLesson> lessons = new ArrayList<>();
}
