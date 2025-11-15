package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.ihqqq.MentorFlow.dto.response.payment.PaymentResponse;
import vn.ihqqq.MentorFlow.entity.payment.Payment;
import vn.ihqqq.MentorFlow.enums.PaymentStatus;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "course.courseId", target = "courseId")
    @Mapping(source = "course.titleCourse", target = "courseName")
    @Mapping(source = "status", target = "statusDisplay", qualifiedByName = "statusToDisplay")
    PaymentResponse toPaymentResponse(Payment payment);

    @Named("statusToDisplay")
    default String statusToDisplay(PaymentStatus status) {
        return status != null ? status.getDisplayName() : null;
    }
}