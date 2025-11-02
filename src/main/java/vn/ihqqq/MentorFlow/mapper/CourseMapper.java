package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.ihqqq.MentorFlow.dto.request.course.CourseCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.CourseUpdateRequest;
import vn.ihqqq.MentorFlow.dto.request.course.ModuleUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.course.CourseResponse;
import vn.ihqqq.MentorFlow.dto.response.course.ModuleWithLessonResponse;
import vn.ihqqq.MentorFlow.entity.course.Course;
import vn.ihqqq.MentorFlow.entity.course.CourseModule;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {
    Course toCourse(CourseCreationRequest course);

    void updateCourse(CourseUpdateRequest request, @MappingTarget Course course);

    CourseResponse toCourseResponse(Course course);


    @Mapping(target = "lessons", source = "lessons")
    ModuleWithLessonResponse toModuleWithLessonResponse(CourseModule module);

}
