package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.*;
import vn.ihqqq.MentorFlow.dto.request.course.CourseCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.CourseUpdateRequest;
import vn.ihqqq.MentorFlow.dto.request.course.ModuleUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.course.CourseResponse;
import vn.ihqqq.MentorFlow.dto.response.course.ModuleWithLessonResponse;
import vn.ihqqq.MentorFlow.entity.course.Course;
import vn.ihqqq.MentorFlow.entity.course.CourseModule;
import vn.ihqqq.MentorFlow.entity.user.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {
    Course toCourse(CourseCreationRequest course);

    void updateCourse(CourseUpdateRequest request, @MappingTarget Course course);

    @Mapping(source = "mentorRequest.id", target = "mentorId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user", target = "mentorName", qualifiedByName = "mapFullName")
    CourseResponse toCourseResponse(Course course);


    @Mapping(target = "lessons", source = "lessons")
    ModuleWithLessonResponse toModuleWithLessonResponse(CourseModule module);


    @Named("mapFullName")
    default String mapFullName(User user) {
        if (user == null) return null;
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }
}
