package vn.ihqqq.MentorFlow.dto.request.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonUpdateRequest {
    @NotBlank(message = "Lesson title cannot be blank")
    @Size(min = 3, max = 100, message = "Lesson title must be between 3 and 100 characters")
    String lessonTitle;

    @Min(value = 1, message = "Lesson time must be at least 1 minute")
    @Max(value = 600, message = "Lesson time must not exceed 600 minutes")
    int timeLesson;
}
