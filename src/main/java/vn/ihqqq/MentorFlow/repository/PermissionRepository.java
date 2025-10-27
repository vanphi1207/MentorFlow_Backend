package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.authentication.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,String> {
}
