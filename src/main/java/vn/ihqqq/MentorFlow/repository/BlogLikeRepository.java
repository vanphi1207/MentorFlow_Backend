package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.blog.BlogLike;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogLikeRepository extends JpaRepository<BlogLike, String> {
    Long countByBlog_BlogId(String blogId);

    Optional<BlogLike> findByBlog_BlogIdAndUser_UserId(String blogId, String userId);

    boolean existsByBlog_BlogIdAndUser_UserId(String blogId, String userId);

    List<BlogLike> findByBlog_BlogId(String blogId);
}
