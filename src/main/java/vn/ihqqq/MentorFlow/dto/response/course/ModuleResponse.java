package vn.ihqqq.MentorFlow.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModuleResponse {
    String moduleId;
    String nameModule;
    String description;
    int timeModule;
    String courseId;
}
