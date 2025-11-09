package vn.ihqqq.MentorFlow.controller.Blog;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.request.Blog.CommentCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.Blog.CommentUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.Blog.CommentResponse;
import vn.ihqqq.MentorFlow.service.Blog.CommentService;

import java.util.List;

@RestController
@RequestMapping("/blogs/{blogId}/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponse> createComment(@PathVariable String blogId,
                                                      @RequestBody CommentCreationRequest request){
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(blogId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable String blogId){
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getCommentsByBlog(blogId))
                .build();
    }

    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> updateComment(@RequestBody CommentUpdateRequest request,
                                                      @PathVariable String commentId){
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(request, commentId))
                .build();
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(@PathVariable String commentId){
        commentService.deleteComment(commentId);
        return ApiResponse.<String>builder()
                .result("Comment deleted")
                .build();
    }

}
