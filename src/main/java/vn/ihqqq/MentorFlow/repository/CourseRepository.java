package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.course.Course;

@Repository
    public interface CourseRepository extends JpaRepository<Course, String> {
        boolean existsByTitleCourse(String titleCourse);
    }
