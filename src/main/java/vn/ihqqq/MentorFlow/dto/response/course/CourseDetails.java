package vn.ihqqq.MentorFlow.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.enums.Level;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDetails {
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
    }
}
