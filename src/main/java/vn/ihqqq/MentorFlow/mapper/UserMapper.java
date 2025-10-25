package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.ihqqq.MentorFlow.dto.request.UserCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.UserUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.UserResponse;
import vn.ihqqq.MentorFlow.entity.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
