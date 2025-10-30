package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.ihqqq.MentorFlow.dto.request.course.CourseCreationRequest;
import vn.ihqqq.MentorFlow.dto.response.course.CourseResponse;
import vn.ihqqq.MentorFlow.entity.course.Course;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {
    Course toCourse(CourseCreationRequest course);

    CourseResponse toCourseResponse(Course course);
}
