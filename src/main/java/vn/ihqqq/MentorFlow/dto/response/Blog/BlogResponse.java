package vn.ihqqq.MentorFlow.dto.response.Blog;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogResponse {
    String blogId;
    BlogOfUserResponse blogOfUser;
    String content;
    String img;
    String timeAgo;
    List<CommentResponse> comments;
    int commentCount;
    Long totalLikes;
    Boolean isLiked;
}
