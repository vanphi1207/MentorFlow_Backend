package vn.ihqqq.MentorFlow.dto.request.course;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import vn.ihqqq.MentorFlow.enums.Level;
import vn.ihqqq.MentorFlow.validator.ValidGender;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseCreationRequest {
    @NotBlank(message = "Tên khóa học không được để trống")
    @Size(min = 10, max = 200, message = "Tên khóa học phải từ 10-200 ký tự")
    String titleCourse;
    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 50, max = 5000, message = "Mô tả phải từ 50-5000 ký tự")
    String description;
    @NotNull(message = "Giá khóa học không được để trống")
    @DecimalMin(value = "10000.00", message = "Số tiền ít nhất phải là 10.000 VNĐ")
    BigDecimal priceCourse;
    @Min(value = 1, message = "Thời lượng khóa học phải lớn hơn 0")
    int timeCourse;
    @NotNull(message = "Level không được để trống")
    Level level;
    int enrolledCount;

}
