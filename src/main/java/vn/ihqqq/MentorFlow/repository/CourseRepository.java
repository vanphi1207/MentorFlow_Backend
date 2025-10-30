package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ihqqq.MentorFlow.entity.course.Course;


    public interface CourseRepository extends JpaRepository<Course, String> {
        boolean existsByTitleCourse(String titleCourse);
    }
