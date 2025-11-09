package vn.ihqqq.MentorFlow.dto.response.Blog;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    CommentOfUserResponse commentOfUserResponse;
    String content;
    String timeAgo;
    int countLike;
    boolean isLiked;
}
