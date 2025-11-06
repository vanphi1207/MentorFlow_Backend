package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.*;
import vn.ihqqq.MentorFlow.dto.request.booking.BookingCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.booking.BookingUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.booking.BookingResponse;
import vn.ihqqq.MentorFlow.entity.booking.Booking;
import vn.ihqqq.MentorFlow.enums.BookingStatus;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE    )
public interface BookingMapper {

    Booking toBooking(BookingCreationRequest request);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "bookAvailability", target = "bookAvailability")
    @Mapping(source = "status", target = "statusDisplay", qualifiedByName = "statusToDisplay")
    BookingResponse toBookingResponse(Booking booking);

    void updateBooking(BookingUpdateRequest request, @MappingTarget Booking booking);
    @Named("statusToDisplay")
    default String statusToDisplay(BookingStatus status) {
        return status != null ? status.getDisplayName() : null;
    }

}
