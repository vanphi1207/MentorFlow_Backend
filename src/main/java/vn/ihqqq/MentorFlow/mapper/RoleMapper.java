package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.ihqqq.MentorFlow.dto.request.RoleRequest;
import vn.ihqqq.MentorFlow.dto.response.RoleResponse;
import vn.ihqqq.MentorFlow.entity.authentication.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
