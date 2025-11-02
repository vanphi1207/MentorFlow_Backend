package vn.ihqqq.MentorFlow.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModuleWithLessonResponse {
    String moduleId;
    String nameModule;
    String description;
    int timeModule;
    List<LessonResponse> lessons;
}
