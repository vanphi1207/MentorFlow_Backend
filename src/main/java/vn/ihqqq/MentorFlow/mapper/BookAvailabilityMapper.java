package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.ihqqq.MentorFlow.dto.request.booking.BookAvailabilityRequest;
import vn.ihqqq.MentorFlow.dto.response.booking.BookAvailabilityResponse;
import vn.ihqqq.MentorFlow.entity.booking.BookAvailability;

@Mapper(componentModel = "spring", uses = {ScheduleSlotMapper.class})
public interface BookAvailabilityMapper {

    BookAvailability toBookAvailability(BookAvailabilityRequest request);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "slot", target = "slot")
    @Mapping(target = "isBooked", expression = "java(bookAvailability.isBooked())")
    BookAvailabilityResponse toBookAvailabilityResponse(BookAvailability bookAvailability);

}
