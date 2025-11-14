package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import vn.ihqqq.MentorFlow.dto.response.Mentor.MentorRequestResponse;
import vn.ihqqq.MentorFlow.entity.user.MentorRequest;

@Mapper(componentModel = "spring")
public interface MentorRequestMapper {
    public default MentorRequestResponse toMentorRequestResponse(MentorRequest mentor) {
        if (mentor == null) return null;

        return MentorRequestResponse.builder()
                .Id(mentor.getId() != null ? mentor.getId().toString() : null)
                .name(mentor.getUser() != null
                        ? mentor.getUser().getFirstName() + " " + mentor.getUser().getLastName()
                        : null)
                .avatar(mentor.getAvatar())
                .linkMeet(mentor.getLinkMeet())
                .companyName(mentor.getCompanyName())
                .position(mentor.getPosition())
                .field(mentor.getField())
                .softSkills(mentor.getSoftSkills())
                .status(mentor.getStatus() != null ? mentor.getStatus().name() : null)
                .build();
    }


}
