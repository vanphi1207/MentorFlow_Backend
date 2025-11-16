package vn.ihqqq.MentorFlow.service.Mentor;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ihqqq.MentorFlow.constant.PredefinedRole;
import vn.ihqqq.MentorFlow.dto.request.Mentor.MentorRequestDTO;
import vn.ihqqq.MentorFlow.dto.response.Mentor.MentorRequestResponse;
import vn.ihqqq.MentorFlow.entity.authentication.Role;
import vn.ihqqq.MentorFlow.entity.user.MentorRequest;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.enums.RequestStatus;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.MentorRequestMapper;
import vn.ihqqq.MentorFlow.repository.MentorRequestRepository;
import vn.ihqqq.MentorFlow.repository.RoleRepository;
import vn.ihqqq.MentorFlow.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MentorRequestService {

    MentorRequestRepository mentorRequestRepository;
    UserRepository userRepository;
    MentorRequestMapper mentorMapper;
    RoleRepository roleRepository;
    Cloudinary cloudinary;

    @Transactional
    public MentorRequestResponse createMentorRequest(String userId, MentorRequestDTO dto,
                                                     MultipartFile avatarFile) throws IOException {
        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra user đã gửi request chưa
        mentorRequestRepository.findByUser_UserId(userId).ifPresent(m -> {
            throw new AppException(ErrorCode.SUBMITTED_REQUEST);
        });

        // Upload avatar nếu có file
        String avatarUrl = null;
        if (avatarFile != null && !avatarFile.isEmpty()) {
            avatarUrl = uploadThumbnail(avatarFile);
        }

        // Tạo mentor request
        MentorRequest mentor = MentorRequest.builder()
                .user(user)
                .linkMeet(dto.getLinkMeet())
                .priceBooking(dto.getPriceBooking())
                .avatar(avatarUrl) // lưu url Cloudinary
                .companyName(dto.getCompanyName())
                .position(dto.getPosition())
                .field(dto.getField())
                .softSkills(dto.getSoftSkills())
                .status(RequestStatus.PENDING)
                .build();

        MentorRequest saved = mentorRequestRepository.save(mentor);
        return mentorMapper.toMentorRequestResponse(saved);
    }

    public String uploadThumbnail(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        String publicValue = generatePublicValue(file.getOriginalFilename());
        String extension = getFileName(file.getOriginalFilename())[1];
        File fileUpload = convert(file);
        log.info("fileUpload is: {}", fileUpload);
        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                "public_id", publicValue
        ));
        cleanDisk(fileUpload);
        return cloudinary.url().generate(publicValue + "." + extension);
    }

    public String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName){
        return originalName.split("\\.");
    }

    public void cleanDisk(File file){
        try {
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convertFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()),
                getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convertFile.toPath());

        }
        return convertFile;
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
        MentorRequest savedRequest  = mentorRequestRepository.save(request);

        User user = request.getUser();

        Role mentorRole = roleRepository.findById(PredefinedRole.MENTOR_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.getRoles().clear();
        user.getRoles().add(mentorRole);

        userRepository.save(user);

        return mentorMapper.toMentorRequestResponse(savedRequest);
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
    public MentorRequestResponse getMentorById(String mentorId) {
        MentorRequest mentor = mentorRequestRepository.findById(mentorId)
                .orElseThrow(() -> new AppException(ErrorCode.MODULE_NOT_FOUND));

        return mentorMapper.toMentorRequestResponse(mentor);
    }


    public MentorRequestResponse getMentorByUserId(String userId) {
        MentorRequest mentor = mentorRequestRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        return mentorMapper.toMentorRequestResponse(mentor);
    }

}
