package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.ihqqq.MentorFlow.dto.request.course.LessonCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.LessonUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.course.LessonResponse;
import vn.ihqqq.MentorFlow.entity.course.CourseLesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    CourseLesson toLesson(LessonCreationRequest course);
    void updateLessonFromRequest(LessonUpdateRequest request, @MappingTarget CourseLesson courseLesson);

    @Mapping(source = "courseModule.moduleId", target = "moduleId")
    LessonResponse toLessonResponse(CourseLesson courseLesson);
}
