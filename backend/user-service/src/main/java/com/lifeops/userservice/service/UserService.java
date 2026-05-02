package com.lifeops.userservice.service;

import com.lifeops.userservice.dto.UserResponse;
import com.lifeops.userservice.entity.User;
import com.lifeops.userservice.exception.UserNotFoundException;
import com.lifeops.userservice.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private static final String DEMO_USER_EMAIL = "vineet@example.com";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getCurrentUser(){
        User user = userRepository
                .findByEmail(DEMO_USER_EMAIL)
                .orElseThrow(()-> new UserNotFoundException(DEMO_USER_EMAIL));

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCountry(),
                user.getProvider()
        );
    }
}
