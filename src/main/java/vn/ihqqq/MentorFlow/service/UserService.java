package vn.ihqqq.MentorFlow.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.dto.request.UserCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.UserUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.UserResponse;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.mapper.UserMapper;
import vn.ihqqq.MentorFlow.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already existed");
        }

        User user = userMapper.toUser(request);

        return userMapper.toUserResponse(userRepository.save(user));

    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll() .stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUserById(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

}
