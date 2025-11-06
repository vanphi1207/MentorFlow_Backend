package vn.ihqqq.MentorFlow.service.booking;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.dto.request.booking.BookingCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.booking.BookingUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.booking.BookingResponse;
import vn.ihqqq.MentorFlow.entity.booking.BookAvailability;
import vn.ihqqq.MentorFlow.entity.booking.Booking;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.enums.BookingStatus;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.BookingMapper;
import vn.ihqqq.MentorFlow.repository.BookAvailabilityRepository;
import vn.ihqqq.MentorFlow.repository.BookingRepository;
import vn.ihqqq.MentorFlow.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingService {

    BookingRepository bookingRepository;
    BookAvailabilityRepository bookAvailabilityRepository;
    UserRepository userRepository;
    BookingMapper bookingMapper;

    // Constants
    private static final int MAX_ACTIVE_BOOKINGS = 5;
    private static final int MIN_BOOKING_ADVANCE_HOURS = 2;

    @Transactional
    public BookingResponse createBooking(BookingCreationRequest request) {
        User student = getCurrentUser();
        BookAvailability availability = validateAndGetAvailability(request.getBookAvailabilityId());

        validateBookingPermission(student, availability);

        validateBookingTime(availability);
        validateMaxActiveBookings(student);
        validateTimeConflict(student, availability);

        Booking booking = buildBooking(student, availability, request);
        Booking savedBooking = bookingRepository.save(booking);

        log.info("Booking created: bookingId={}, studentId={}, mentorId={}, date={}, slot={}",
                savedBooking.getBookingId(),
                student.getUserId(),
                availability.getUser().getUserId(),
                availability.getDate(),
                availability.getSlot().getSlotId()
        );

        return bookingMapper.toBookingResponse(savedBooking);
    }

    public BookingResponse getBookingById(String bookingId) {
        Booking booking = findBookingById(bookingId);
        return bookingMapper.toBookingResponse(booking);
    }

    public List<BookingResponse> getMyBookings() {
        User user = getCurrentUser();
        return bookingRepository.findByUser_UserId(user.getUserId())
                .stream()
                .map(bookingMapper::toBookingResponse)
                .toList();
    }

    public List<BookingResponse> getMyBookingsByStatus(BookingStatus status) {
        User user = getCurrentUser();
        return bookingRepository.findByUser_UserIdAndStatus(user.getUserId(), status)
                .stream()
                .map(bookingMapper::toBookingResponse)
                .toList();
    }

    public List<BookingResponse> getBookingsForMentor(LocalDate startDate, LocalDate endDate) {
        User mentor = getCurrentUser();
        return bookingRepository.findByMentorAndDateRange(
                        mentor.getUserId(), startDate, endDate)
                .stream()
                .map(bookingMapper::toBookingResponse)
                .toList();
    }

    @Transactional
    public BookingResponse updateBooking(String bookingId, BookingUpdateRequest request) {
        User currentUser = getCurrentUser();
        Booking booking = findBookingById(bookingId);

        // Validate permission: Only student or mentor can update
        validateUpdatePermission(currentUser, booking);

        bookingMapper.updateBooking(request, booking);
        Booking updatedBooking = bookingRepository.save(booking);

        log.info("Booking updated: bookingId={}, updatedBy={}", bookingId, currentUser.getUserId());

        return bookingMapper.toBookingResponse(updatedBooking);
    }

    @Transactional
    public void cancelBooking(String bookingId, String cancelReason) {
        User currentUser = getCurrentUser();
        Booking booking = findBookingById(bookingId);

        validateCancelPermission(currentUser, booking);

        if (BookingStatus.COMPLETED.equals(booking.getStatus()) ||
                BookingStatus.CANCELLED.equals(booking.getStatus())) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_BOOKING);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.warn("Booking cancelled: bookingId={}, userId={}, reason={}",
                bookingId, currentUser.getUserId(), cancelReason);
    }

    @Transactional
    public BookingResponse confirmBooking(String bookingId) {
        User mentor = getCurrentUser();
        Booking booking = findBookingById(bookingId);

        if (!booking.getBookAvailability().getUser().getUserId().equals(mentor.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED_BOOKING_ACCESS);
        }

        if (!BookingStatus.PENDING.equals(booking.getStatus())) {
            throw new AppException(ErrorCode.BOOKING_NOT_PENDING);
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        Booking confirmedBooking = bookingRepository.save(booking);

        log.info("Booking confirmed: bookingId={}, mentorId={}", bookingId, mentor.getUserId());

        return bookingMapper.toBookingResponse(confirmedBooking);
    }

    @Transactional
    public BookingResponse completeBooking(String bookingId) {
        User mentor = getCurrentUser();
        Booking booking = findBookingById(bookingId);

        if (!booking.getBookAvailability().getUser().getUserId().equals(mentor.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED_BOOKING_ACCESS);
        }

        if (!BookingStatus.CONFIRMED.equals(booking.getStatus())) {
            throw new AppException(ErrorCode.BOOKING_NOT_CONFIRMED);
        }

        booking.setStatus(BookingStatus.COMPLETED);
        Booking completedBooking = bookingRepository.save(booking);

        log.info("Booking completed: bookingId={}, mentorId={}", bookingId, mentor.getUserId());

        return bookingMapper.toBookingResponse(completedBooking);
    }


    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private Booking findBookingById(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
    }

    private BookAvailability validateAndGetAvailability(String availabilityId) {
        BookAvailability availability = bookAvailabilityRepository
                .findById(availabilityId)
                .orElseThrow(() -> new AppException(ErrorCode.AVAILABILITY_NOT_FOUND));

        if (availability.isBooked()) {
            throw new AppException(ErrorCode.SLOT_ALREADY_BOOKED);
        }

        return availability;
    }

    private void validateBookingPermission(User student, BookAvailability availability) {
        // Cannot book own slot
        if (availability.getUser().getUserId().equals(student.getUserId())) {
            throw new AppException(ErrorCode.CANNOT_BOOK_OWN_SLOT);
        }
    }

    private void validateBookingTime(BookAvailability availability) {
        LocalDateTime bookingDateTime = LocalDateTime.of(
                availability.getDate(),
                availability.getSlot().getStartTime()
        );

        // Cannot book past slots
        if (bookingDateTime.isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.BOOKING_PAST_DATE);
        }

        // Must book at least X hours in advance
        if (LocalDateTime.now().plusHours(MIN_BOOKING_ADVANCE_HOURS)
                .isAfter(bookingDateTime)) {
            throw new AppException(ErrorCode.BOOKING_TOO_CLOSE);
        }
    }

    private void validateMaxActiveBookings(User student) {
        long activeBookings = bookingRepository
                .findByUser_UserId(student.getUserId())
                .stream()
                .filter(b -> BookingStatus.PENDING.equals(b.getStatus()) ||
                        BookingStatus.CONFIRMED.equals(b.getStatus()))
                .count();

        if (activeBookings >= MAX_ACTIVE_BOOKINGS) {
            throw new AppException(ErrorCode.MAX_BOOKINGS_REACHED);
        }
    }

    private void validateTimeConflict(User student, BookAvailability availability) {
        LocalDate bookingDate = availability.getDate();
        LocalTime bookingStart = availability.getSlot().getStartTime();
        LocalTime bookingEnd = availability.getSlot().getEndTime();

        List<Booking> existingBookings = bookingRepository
                .findByUser_UserIdAndDateBook(student.getUserId(), bookingDate);

        for (Booking existing : existingBookings) {
            // Skip cancelled bookings
            if (BookingStatus.CANCELLED.equals(existing.getStatus())) {
                continue;
            }

            BookAvailability existingAvail = existing.getBookAvailability();
            LocalTime existingStart = existingAvail.getSlot().getStartTime();
            LocalTime existingEnd = existingAvail.getSlot().getEndTime();

            // Check overlap
            if (bookingStart.isBefore(existingEnd) && bookingEnd.isAfter(existingStart)) {
                throw new AppException(ErrorCode.BOOKING_TIME_CONFLICT);
            }
        }
    }

    private Booking buildBooking(User student, BookAvailability availability,
                                 BookingCreationRequest request) {
        return Booking.builder()
                .user(student)
                .bookAvailability(availability)
                .topic(request.getTopic())
                .connectionForm(request.getConnectionForm())
                .note(request.getNote())
                .dateBook(availability.getDate())
                .time(availability.getSlot().getStartTime())
                .status(BookingStatus.PENDING)
                .build();
    }

    private void validateUpdatePermission(User currentUser, Booking booking) {
        boolean isStudent = booking.getUser().getUserId().equals(currentUser.getUserId());
        boolean isMentor = booking.getBookAvailability().getUser().getUserId()
                .equals(currentUser.getUserId());

        if (!isStudent && !isMentor) {
            throw new AppException(ErrorCode.UNAUTHORIZED_BOOKING_ACCESS);
        }
    }

    private void validateCancelPermission(User currentUser, Booking booking) {
        boolean isStudent = booking.getUser().getUserId().equals(currentUser.getUserId());
        boolean isMentor = booking.getBookAvailability().getUser().getUserId()
                .equals(currentUser.getUserId());

        if (!isStudent && !isMentor) {
            throw new AppException(ErrorCode.UNAUTHORIZED_BOOKING_ACCESS);
        }
    }
}