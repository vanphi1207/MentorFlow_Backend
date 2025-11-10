package vn.ihqqq.MentorFlow.service.Blog;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.dto.response.Blog.BlogLikeResponse;
import vn.ihqqq.MentorFlow.entity.blog.Blog;
import vn.ihqqq.MentorFlow.entity.blog.BlogLike;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.repository.BlogLikeRepository;
import vn.ihqqq.MentorFlow.repository.BlogRepository;
import vn.ihqqq.MentorFlow.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BlogLikeService {

    BlogLikeRepository blogLikeRepository;
    BlogRepository blogRepository;
    UserRepository userRepository;

    public Long getTotalLikes(String blogId) {
        return blogLikeRepository.countByBlog_BlogId(blogId);
    }

    public boolean isUserLiked(String blogId, String userId) {
        return blogLikeRepository.existsByBlog_BlogIdAndUser_UserId(blogId, userId);
    }

    @Transactional
    public BlogLikeResponse likeBlog(String blogId, String userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        // Kiểm tra user tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra đã like chưa
        if (blogLikeRepository.existsByBlog_BlogIdAndUser_UserId(blogId, userId)) {
            throw new AppException(ErrorCode.BLOG_DONT_LIKE);
        }

        // Tạo like mới
        BlogLike blogLike = new BlogLike();
        blogLike.setBlog(blog);
        blogLike.setUser(user);
        blogLike.setCreatedAt(LocalDateTime.now());
        blogLikeRepository.save(blogLike);

        // Trả về response
        Long totalLikes = blogLikeRepository.countByBlog_BlogId(blogId);
        return new BlogLikeResponse(true, totalLikes);
    }

    @Transactional
    public BlogLikeResponse unlikeBlog(String blogId, String userId) {
        BlogLike blogLike = blogLikeRepository.findByBlog_BlogIdAndUser_UserId(blogId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_LIKE));

        blogLikeRepository.delete(blogLike);

        Long totalLikes = blogLikeRepository.countByBlog_BlogId(blogId);
        return new BlogLikeResponse(false, totalLikes);
    }
}
