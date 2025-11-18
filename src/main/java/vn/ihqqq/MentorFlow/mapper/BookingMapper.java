package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.*;
import vn.ihqqq.MentorFlow.dto.request.booking.BookingCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.booking.BookingUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.booking.BookingResponse;
import vn.ihqqq.MentorFlow.entity.booking.Booking;
import vn.ihqqq.MentorFlow.enums.BookingStatus;
import vn.ihqqq.MentorFlow.repository.MentorRequestRepository;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {

    Booking toBooking(BookingCreationRequest request);

    @Mapping(source = "user.userId", target = "studentId")
    @Mapping(source = "user", target = "studentName", qualifiedByName = "getStudentFullName")
    @Mapping(source = "user.email", target = "studentEmail")
    @Mapping(source = "bookAvailability.bookAvailabilityId", target = "bookAvailability.bookAvailabilityId")
    @Mapping(source = "bookAvailability.user.mentor.id", target = "bookAvailability.mentorId")
    @Mapping(source = "bookAvailability.user.mentor.avatar", target = "bookAvailability.mentorAvatar")
    @Mapping(source = "bookAvailability.user.mentor.linkMeet", target = "bookAvailability.linkMeet")
    @Mapping(source = "bookAvailability.user", target = "bookAvailability.fullName", qualifiedByName = "getMentorFullName")
    @Mapping(source = "bookAvailability.date", target = "bookAvailability.date")
    @Mapping(source = "bookAvailability.slot", target = "bookAvailability.slot")
//    @Mapping(target = "bookAvailability.isBooked", expression = "java(booking.getBookAvailability() != null && booking.getBookAvailability().isBooked())")
    @Mapping(source = "status", target = "statusDisplay", qualifiedByName = "statusToDisplay")
    BookingResponse toBookingResponse(Booking booking);

    void updateBooking(BookingUpdateRequest request, @MappingTarget Booking booking);

    @Named("statusToDisplay")
    default String statusToDisplay(BookingStatus status) {
        return status != null ? status.getDisplayName() : null;
    }

    @Named("getMentorFullName")
    default String getMentorFullName(vn.ihqqq.MentorFlow.entity.user.User user) {
        if (user == null) {
            return null;
        }
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }

    @Named("getStudentFullName")
    default String getStudentFullName(vn.ihqqq.MentorFlow.entity.user.User user) {
        if (user == null) {
            return null;
        }
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }
}