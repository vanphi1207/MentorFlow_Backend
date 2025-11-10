package vn.ihqqq.MentorFlow.service.Blog;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.dto.request.Blog.CommentCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.Blog.CommentUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.Blog.CommentResponse;
import vn.ihqqq.MentorFlow.entity.blog.Blog;
import vn.ihqqq.MentorFlow.entity.blog.BlogComment;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.BlogCommentMapper;
import vn.ihqqq.MentorFlow.repository.BlogRepository;
import vn.ihqqq.MentorFlow.repository.CommentRespository;
import vn.ihqqq.MentorFlow.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentService {
    CommentRespository commentRespository;
    BlogRepository blogRepository;
    UserService userService;
    BlogCommentMapper blogCommentMapper;

    public CommentResponse createComment(String blogId, CommentCreationRequest request) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        User currentUser = userService.getCurrentUser();

        BlogComment comment = BlogComment.builder()
                .blog(blog)
                .user(currentUser)
                .content(request.getContent())
                .build();

        BlogComment savedComment = commentRespository.save(comment);
        return  blogCommentMapper.toCommentResponse(savedComment);
    }

    public List<CommentResponse> getCommentsByBlog(String blogId) {
        List<BlogComment> comments = commentRespository.findByBlogBlogId(blogId);
        return comments.stream()
                .map(blogCommentMapper::toCommentResponse)
                .toList();
    }

    public int countCommentsByBlog(String blogId) {
        return commentRespository.countByBlogBlogId(blogId);
    }

    public CommentResponse updateComment(CommentUpdateRequest request, String id){
        BlogComment comment = commentRespository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        User currentUser = userService.getCurrentUser();
        if (!comment.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        comment.setContent(request.getContent());

        BlogComment updated = commentRespository.save(comment);

        return blogCommentMapper.toCommentResponse(updated);
    }

    public void deleteComment(String id){
        BlogComment comment = commentRespository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        commentRespository.delete(comment);
    }


}
