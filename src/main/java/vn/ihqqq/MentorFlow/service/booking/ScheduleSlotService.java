package vn.ihqqq.MentorFlow.service.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.dto.request.booking.ScheduleSlotRequest;
import vn.ihqqq.MentorFlow.dto.response.booking.ScheduleSlotResponse;
import vn.ihqqq.MentorFlow.entity.booking.ScheduleSlot;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.ScheduleSlotMapper;
import vn.ihqqq.MentorFlow.repository.ScheduleSlotRepository;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleSlotService {

    ScheduleSlotRepository scheduleSlotRepository;
    ScheduleSlotMapper scheduleSlotMapper;

    private static final int MIN_SLOT_DURATION_MINUTES = 30;
    private static final int MAX_SLOT_DURATION_MINUTES = 180; // 3 hours

    public ScheduleSlotResponse createSlot(ScheduleSlotRequest request) {
        LocalTime startTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();

        validateSlotTime(startTime, endTime);

        if (scheduleSlotRepository.existsByStartTimeAndEndTime(startTime, endTime)) {
            throw new AppException(ErrorCode.SLOT_ALREADY_EXISTS);
        }

        ScheduleSlot slot = scheduleSlotMapper.toScheduleSlot(request);
        ScheduleSlot savedSlot = scheduleSlotRepository.save(slot);

        log.info("Schedule slot created: slotId={}, time={} - {}",
                savedSlot.getSlotId(), startTime, endTime);

        return scheduleSlotMapper.toScheduleSlotResponse(savedSlot);
    }

    public List<ScheduleSlotResponse> getAllSlots() {
        return scheduleSlotRepository.findAll()
                .stream()
                .map(scheduleSlotMapper::toScheduleSlotResponse)
                .toList();
    }

    public ScheduleSlotResponse getSlotById(String slotId) {
        ScheduleSlot slot = scheduleSlotRepository.findById(slotId)
                .orElseThrow(() -> new AppException(ErrorCode.SLOT_NOT_FOUND));

        return scheduleSlotMapper.toScheduleSlotResponse(slot);
    }

    public void deleteSlot(String slotId) {
        if (!scheduleSlotRepository.existsById(slotId)) {
            throw new AppException(ErrorCode.SLOT_NOT_FOUND);
        }

        scheduleSlotRepository.deleteById(slotId);
        log.info("Schedule slot deleted: slotId={}", slotId);
    }


    private void validateSlotTime(LocalTime startTime, LocalTime endTime) {
        // Start must be before end
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new AppException(ErrorCode.SLOT_TIME_INVALID);
        }

        long durationMinutes = ChronoUnit.MINUTES.between(startTime, endTime);

        if (durationMinutes < MIN_SLOT_DURATION_MINUTES) {
            throw new AppException(ErrorCode.SLOT_DURATION_INVALID);
        }

        if (durationMinutes > MAX_SLOT_DURATION_MINUTES) {
            log.warn("Slot duration exceeds recommended maximum: {} minutes", durationMinutes);
        }

        if (startTime.isBefore(LocalTime.of(6, 0)) ||
                endTime.isAfter(LocalTime.of(23, 0))) {
            log.warn("Slot time outside normal working hours: {} - {}", startTime, endTime);
        }
    }
}