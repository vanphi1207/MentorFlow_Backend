package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.authentication.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
}
