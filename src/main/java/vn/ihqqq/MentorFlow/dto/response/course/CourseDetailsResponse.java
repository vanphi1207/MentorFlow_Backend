package vn.ihqqq.MentorFlow.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.enums.Level;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDetailsResponse {
    String courseId;
    String titleCourse;
    String description;
    BigDecimal priceCourse;
    int timeCourse;
    Level level;
    int enrolledCount;
    String thumbnailImg;
    String videoDemo;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    List<ModuleWithLessonResponse> modules;
}
