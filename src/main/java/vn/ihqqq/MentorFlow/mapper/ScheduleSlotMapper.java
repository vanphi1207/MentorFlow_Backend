package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import vn.ihqqq.MentorFlow.dto.request.booking.ScheduleSlotRequest;
import vn.ihqqq.MentorFlow.dto.response.booking.ScheduleSlotResponse;
import vn.ihqqq.MentorFlow.entity.booking.ScheduleSlot;

@Mapper(componentModel = "spring")
public interface ScheduleSlotMapper {

    ScheduleSlot toScheduleSlot(ScheduleSlotRequest request);
    ScheduleSlotResponse toScheduleSlotResponse(ScheduleSlot scheduleSlot);

}
