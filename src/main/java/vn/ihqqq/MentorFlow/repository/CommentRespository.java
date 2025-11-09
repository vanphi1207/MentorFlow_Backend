package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.blog.BlogComment;

import java.util.List;

@Repository
public interface CommentRespository extends JpaRepository<BlogComment, String> {
    List<BlogComment> findByBlogBlogId(String blogId);
    int countByBlogBlogId(String blogId);
}
