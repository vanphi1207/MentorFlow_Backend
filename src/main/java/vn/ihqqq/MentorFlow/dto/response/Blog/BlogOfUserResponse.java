package vn.ihqqq.MentorFlow.dto.response.Blog;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogOfUserResponse {
    String userId;
    String fullName;
    String avatar;
}
