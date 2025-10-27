package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import vn.ihqqq.MentorFlow.dto.request.PermissionRequest;
import vn.ihqqq.MentorFlow.dto.response.PermissionResponse;
import vn.ihqqq.MentorFlow.entity.authentication.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
