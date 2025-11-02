package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.course.CourseLesson;

@Repository
public interface LessonRepository extends JpaRepository<CourseLesson, String> {
    boolean existsByLessonTitle(String lessonTitle);
}
