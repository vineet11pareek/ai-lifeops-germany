package com.lifeops.userservice.service;

import com.lifeops.userservice.dto.AuthenticatedUser;
import com.lifeops.userservice.dto.CreateUserRequest;
import com.lifeops.userservice.dto.UserResponse;
import com.lifeops.userservice.entity.User;
import com.lifeops.userservice.exception.DuplicateUserException;
import com.lifeops.userservice.exception.UserNotFoundException;
import com.lifeops.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getOrCreateAuthenticatedUser(AuthenticatedUser authenticatedUser){
        log.info("Loading authenticated user with email={}", authenticatedUser.email());
        User user = userRepository.findByEmail(authenticatedUser.email())
                .orElseGet(()-> {
                    log.info("Creating new authenticated user with email={}",authenticatedUser.email());

                    User newUser = new User(
                            authenticatedUser.externalId(),
                            authenticatedUser.fullName(),
                            authenticatedUser.email(),
                            "Germany",
                            authenticatedUser.provider()

                    );
                    return userRepository.save(newUser);
                });
        return toResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request){
        log.info("Creating user with email={}", request.email());
        if(userRepository.existsByEmail(request.email())){
            log.warn("User creation failed because email already exists: {}", request.email());
            throw new DuplicateUserException(request.email());
        }

        User user = new User(
                null,
                request.fullName(),
                request.email(),
                request.country(),
                request.provider()
        );

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCountry(),
                user.getProvider()
        );
    }
}
