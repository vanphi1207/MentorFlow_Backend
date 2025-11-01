package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.course.CourseModule;

@Repository
public interface ModuleRepository extends JpaRepository<CourseModule, String> {
    boolean existsByNameModule(String nameModule);
}
