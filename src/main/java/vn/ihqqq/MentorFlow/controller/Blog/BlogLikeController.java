package vn.ihqqq.MentorFlow.controller.Blog;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.Blog.BlogLikeResponse;
import vn.ihqqq.MentorFlow.service.Blog.BlogLikeService;
import vn.ihqqq.MentorFlow.service.UserService;


@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogLikeController {

    BlogLikeService blogLikeService;
    UserService userService;

    @GetMapping("/{blogId}/likes/count")
    public ApiResponse<Long> getTotalLikes(@PathVariable String blogId) {
        Long totalLikes = blogLikeService.getTotalLikes(blogId);
        return ApiResponse.<Long>builder()
                .result(totalLikes)
                .build();
    }

    @GetMapping("/{blogId}/likes/check")
    public ApiResponse<Boolean> checkUserLiked(@PathVariable String blogId) {
        String currentUserId = userService.getCurrentUser().getUserId();
        boolean isLiked = blogLikeService.isUserLiked(blogId, currentUserId);
        return ApiResponse.<Boolean>builder()
                .result(isLiked)
                .build();
    }

    @PostMapping("/{blogId}/like")
    public ApiResponse<BlogLikeResponse> likeBlog(@PathVariable String blogId) {
        String currentUserId = userService.getCurrentUser().getUserId();
        BlogLikeResponse response = blogLikeService.likeBlog(blogId, currentUserId);
        return ApiResponse.<BlogLikeResponse>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{blogId}/like")
    public ApiResponse<BlogLikeResponse> unlikeBlog(@PathVariable String blogId) {
        String currentUserId = userService.getCurrentUser().getUserId();
        BlogLikeResponse response = blogLikeService.unlikeBlog(blogId, currentUserId);
        return ApiResponse.<BlogLikeResponse>builder()
                .result(response)
                .build();
    }

}
