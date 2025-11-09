package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.ihqqq.MentorFlow.dto.request.Blog.BlogCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.Blog.BlogUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.Blog.BlogResponse;
import vn.ihqqq.MentorFlow.entity.blog.Blog;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    Blog toBlog(BlogCreationRequest request);

    void updateBlog(BlogUpdateRequest request, @MappingTarget Blog blog);

    @Mapping(target = "blogOfUser.userId", source = "user.userId")
    @Mapping(target = "blogOfUser.fullName", source = "user.lastName")
    @Mapping(target = "blogOfUser.avatar", source = "user.avt")
    @Mapping(target = "timeAgo", expression = "java(vn.ihqqq.MentorFlow.utils.TimeAgoUtils.calculateTimeAgo(blog.getCreatedAt()))")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    BlogResponse toBlogResponse(Blog blog);

}
