package com.ridebooking.user.service.impl;

import com.ridebooking.user.dto.CreateUserRequest;
import com.ridebooking.user.dto.UpdateUserRequest;
import com.ridebooking.user.dto.UserResponse;
import com.ridebooking.user.entity.User;
import com.ridebooking.user.repository.UserRepository;
import com.ridebooking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(Long id, CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .id(id)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .profileImage(request.getProfileImage())
                .active(true)
                .build();

        user = userRepository.save(user);

        return mapToResponse(user);

    }
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()

                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .active(user.isActive())
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }

        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        user = userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }
}