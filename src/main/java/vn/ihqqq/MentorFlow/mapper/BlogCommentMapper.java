package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.ihqqq.MentorFlow.dto.request.Blog.CommentCreationRequest;
import vn.ihqqq.MentorFlow.dto.response.Blog.CommentResponse;
import vn.ihqqq.MentorFlow.entity.blog.BlogComment;
import vn.ihqqq.MentorFlow.utils.TimeAgoUtils;

@Mapper(componentModel = "spring")
public interface BlogCommentMapper {
    BlogComment toComment(CommentCreationRequest request);

    @Mapping(target = "timeAgo", source = "createdAt", qualifiedByName = "mapTimeAgo")
    @Mapping(target = "commentOfUserResponse.userId", source = "user.userId")
    @Mapping(target = "commentOfUserResponse.fullName", source = "user.lastName")
    @Mapping(target = "commentOfUserResponse.avatar", source = "user.avt")
    CommentResponse toCommentResponse(BlogComment comment);

    @Named("mapTimeAgo")
    default String mapTimeAgo(java.time.LocalDateTime createdAt) {
        return TimeAgoUtils.calculateTimeAgo(createdAt);
    }
}
