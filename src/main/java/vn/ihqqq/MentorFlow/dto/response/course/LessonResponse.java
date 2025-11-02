package vn.ihqqq.MentorFlow.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonResponse {
    String lessonId;
    String lessonTitle;
    int timeLesson;
    String videoURL;
    String moduleId;
}
