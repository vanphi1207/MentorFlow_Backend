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
public class ModuleUpdateRequest {
    @NotBlank(message = "Tên module không được để trống")
    @Size(min = 3, max = 100, message = "Tên module phải có độ dài từ 3 đến 100 ký tự")
    String nameModule;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, max = 500, message = "Mô tả phải có độ dài từ 10 đến 500 ký tự")
    String description;

    @Min(value = 1, message = "Thời lượng module phải lớn hơn hoặc bằng 1 giờ")
    @Max(value = 1000, message = "Thời lượng module không được vượt quá 1000 giờ")
    int timeModule;
}
