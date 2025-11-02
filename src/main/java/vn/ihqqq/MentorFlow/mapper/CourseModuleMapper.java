package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.ihqqq.MentorFlow.dto.request.course.ModuleCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.ModuleUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.course.ModuleResponse;
import vn.ihqqq.MentorFlow.entity.course.CourseModule;

@Mapper(componentModel = "spring")
public interface CourseModuleMapper {
    CourseModule toModuleCourse(ModuleCreationRequest request);

    void updateModuleFromRequest(ModuleUpdateRequest request, @MappingTarget CourseModule module);

    @Mapping(source = "course.courseId", target = "courseId")
    ModuleResponse toModuleCourseResponse(CourseModule courseModule);
}
