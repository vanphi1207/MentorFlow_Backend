package vn.ihqqq.MentorFlow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import vn.ihqqq.MentorFlow.dto.request.UserCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.UserUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.UserResponse;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.enums.Gender;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "genderDisplay", source = "gender", qualifiedByName = "genderToDisplay")
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Named("genderToDisplay")
    default String genderToDisplay(Gender gender) {
        return gender != null ? gender.getDisplayName() : null;
    }
}
