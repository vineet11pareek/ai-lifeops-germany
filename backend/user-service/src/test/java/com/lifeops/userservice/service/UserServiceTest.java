package com.lifeops.userservice.service;

import com.lifeops.userservice.dto.CreateUserRequest;
import com.lifeops.userservice.dto.UserResponse;
import com.lifeops.userservice.entity.User;
import com.lifeops.userservice.exception.DuplicateUserException;
import com.lifeops.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully(){
        CreateUserRequest request = new CreateUserRequest(
                "Test User",
                "test.user@example.com",
                "Germany",
                "Google"
        );

        User user = new User(
                request.fullName(),
                request.email(),
                request.country(),
                request.provider()
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save((any(User.class)))).thenReturn(user);

        UserResponse response = userService.createUser(request);

        assertThat(response.name()).isEqualTo("Test User");
        assertThat(response.email()).isEqualTo("test.user@example.com");
        assertThat(response.country()).isEqualTo("Germany");
        assertThat(response.provider()).isEqualTo("Google");

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists(){
        CreateUserRequest request = new CreateUserRequest(
                "Test User",
                "test.user@example.com",
                "Germany",
                "Google"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(()-> userService.createUser(request))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("test.user@example.com");

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository,never()).save(any(User.class));


    }
}
