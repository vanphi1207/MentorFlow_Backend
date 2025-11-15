package vn.ihqqq.MentorFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ihqqq.MentorFlow.entity.user.MentorRequest;
import vn.ihqqq.MentorFlow.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRequestRepository extends JpaRepository<MentorRequest, String> {
    Optional<MentorRequest> findByUser_UserId(String userId);

    List<MentorRequest> findByStatus(RequestStatus status);

}
