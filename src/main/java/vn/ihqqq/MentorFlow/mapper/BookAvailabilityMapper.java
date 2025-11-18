package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.ihqqq.MentorFlow.dto.request.booking.BookAvailabilityRequest;
import vn.ihqqq.MentorFlow.dto.response.booking.BookAvailabilityResponse;
import vn.ihqqq.MentorFlow.entity.booking.BookAvailability;
import vn.ihqqq.MentorFlow.entity.user.User;

@Mapper(componentModel = "spring", uses = {ScheduleSlotMapper.class})
public interface BookAvailabilityMapper {

    BookAvailability toBookAvailability(BookAvailabilityRequest request);

    @Mapping(source = "user.mentor.id", target = "mentorId")
    @Mapping(source = "user", target = "fullName", qualifiedByName = "getFullName")
    @Mapping(source = "user.mentor.linkMeet", target = "linkMeet")
    @Mapping(source = "slot", target = "slot")
    @Mapping(source = "user.mentor.avatar", target = "mentorAvatar")
    BookAvailabilityResponse toBookAvailabilityResponse(BookAvailability bookAvailability);


    @Named("getFullName")
    default String getFullName(User user) {
        if (user == null) {
            return null;
        }

        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";

        return (firstName + " " + lastName).trim();
    }
}
