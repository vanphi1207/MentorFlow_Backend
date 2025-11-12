package vn.ihqqq.MentorFlow.service.Mentor;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.dto.request.Mentor.MentorRequestDTO;
import vn.ihqqq.MentorFlow.dto.response.Mentor.MentorRequestResponse;
import vn.ihqqq.MentorFlow.entity.user.MentorRequest;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.enums.RequestStatus;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.MentorRequestMapper;
import vn.ihqqq.MentorFlow.repository.MentorRequestRepository;
import vn.ihqqq.MentorFlow.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MentorRequestService {

    MentorRequestRepository mentorRequestRepository;
    UserRepository userRepository;
    MentorRequestMapper mentorMapper;

    public MentorRequestResponse createMentorRequest(String userId, MentorRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        mentorRequestRepository.findByUser_UserId(userId).ifPresent(m -> {
            throw new AppException(ErrorCode.SUBMITTED_REQUEST);
        });

        MentorRequest mentor = MentorRequest.builder()
                .user(user)
                .linkMeet(dto.getLinkMeet())
                .avatar(dto.getAvatar())
                .companyName(dto.getCompanyName())
                .position(dto.getPosition())
                .field(dto.getField())
                .softSkills(dto.getSoftSkills())
                .status(RequestStatus.PENDING)
                .build();

        MentorRequest saved = mentorRequestRepository.save(mentor);
        return mentorMapper.toMentorRequestResponse(saved);
    }

    //duyet yeu cau
    @PreAuthorize("hasRole('ADMIN')")
    public MentorRequestResponse approveRequest(String requestId) {
        MentorRequest request = mentorRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AppException(ErrorCode.REQUEST_BAD_REQUEST);
        }

        request.setStatus(RequestStatus.APPROVED);
        MentorRequest saved = mentorRequestRepository.save(request);
        return mentorMapper.toMentorRequestResponse(saved);
    }

    //tu choi yeu cau
    @PreAuthorize("hasRole('ADMIN')")
    public MentorRequestResponse rejectRequest(String requestId) {
        MentorRequest request = mentorRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AppException(ErrorCode.REQUEST_BAD_REQUEST);
        }

        request.setStatus(RequestStatus.REJECTED);
        MentorRequest saved = mentorRequestRepository.save(request);
        return mentorMapper.toMentorRequestResponse(saved);
    }

    public List<MentorRequestResponse> getAllRequests(RequestStatus status) {
        List<MentorRequest> requests;
        if (status != null) {
            requests = mentorRequestRepository.findByStatus(status);
        } else {
            requests = mentorRequestRepository.findAll();
        }

        return requests.stream()
                .map(mentorMapper::toMentorRequestResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRequest(String requestId) {
        MentorRequest request = mentorRequestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        mentorRequestRepository.delete(request);
    }
}
