package vn.ihqqq.MentorFlow.service.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.dto.request.booking.BookAvailabilityRequest;
import vn.ihqqq.MentorFlow.dto.response.booking.BookAvailabilityResponse;
import vn.ihqqq.MentorFlow.entity.booking.BookAvailability;
import vn.ihqqq.MentorFlow.entity.booking.ScheduleSlot;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.BookAvailabilityMapper;
import vn.ihqqq.MentorFlow.repository.BookAvailabilityRepository;
import vn.ihqqq.MentorFlow.repository.ScheduleSlotRepository;
import vn.ihqqq.MentorFlow.repository.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookAvailabilityService {
    BookAvailabilityRepository bookAvailabilityRepository;
    ScheduleSlotRepository scheduleSlotRepository;
    UserRepository userRepository;
    BookAvailabilityMapper bookAvailabilityMapper;

    public BookAvailabilityResponse createAvailability(BookAvailabilityRequest request) {
        User mentor = getCurrentUser();

        validateAvailabilityDate(request.getDate());

        // Validate slot tồn tại
        ScheduleSlot slot = scheduleSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new AppException(ErrorCode.SLOT_NOT_FOUND));

        // Check xem mentor đã tạo availability cho slot này vào ngày này chưa
        if (bookAvailabilityRepository.findByUserAndDateAndSlot(
                mentor.getUserId(), request.getDate(), request.getSlotId()).isPresent()) {
            throw new AppException(ErrorCode.SLOT_ALREADY_EXISTS);
        }

        BookAvailability availability = BookAvailability.builder()
                .user(mentor)
                .slot(slot)
                .date(request.getDate())
                .build();

        BookAvailability savedAvailability = bookAvailabilityRepository.save(availability);

        return bookAvailabilityMapper.toBookAvailabilityResponse(savedAvailability);
    }

    public List<BookAvailabilityResponse> getMyAvailabilities() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User mentor = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return bookAvailabilityRepository.findByUser_UserId(mentor.getUserId())
                .stream()
                .map(bookAvailabilityMapper::toBookAvailabilityResponse)
                .toList();
    }

    public List<BookAvailabilityResponse> getAvailableSlotsByMentor(String userId) {
        return bookAvailabilityRepository.findBookAvailabilityByUserId(userId)
                .stream()
                .map(bookAvailabilityMapper::toBookAvailabilityResponse)
                .toList();
    }

    public List<BookAvailabilityResponse> getAvailableSlotsByMentorAndDate(
            String mentorId, LocalDate date) {

        List<BookAvailability> availabilities =
                bookAvailabilityRepository.findByUser_UserIdAndDate(mentorId, date);

        if (date.isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.AVAILABILITY_PAST_DATE);
        }

        // Filter chỉ lấy những slot chưa bị book
        return availabilities.stream()
                .filter(a -> !a.isBooked())
                .map(bookAvailabilityMapper::toBookAvailabilityResponse)
                .toList();
    }

    public void deleteAvailability(String availabilityId) {
        User mentor = getCurrentUser();
        BookAvailability availability = bookAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new AppException(ErrorCode.AVAILABILITY_NOT_FOUND));

        if (!availability.getUser().getUserId().equals(mentor.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED_BOOKING_ACCESS);
        }

        // Check xem đã bị book chưa
        if (availability.isBooked()) {
            throw new AppException(ErrorCode.SLOT_ALREADY_BOOKED);
        }

        bookAvailabilityRepository.deleteById(availabilityId);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private void validateAvailabilityDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.AVAILABILITY_PAST_DATE);
        }

        if(date.isAfter(LocalDate.now().plusMonths(3))) {
            log.warn("Creating availability more than 3 months in advance: {}", date);
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.AVAILABILITY_PAST_DATE);
        }

        if (ChronoUnit.DAYS.between(startDate, endDate) > 90) {
            log.warn("Date range exceeds 90 days: {} to {}", startDate, endDate);
        }
    }

}
