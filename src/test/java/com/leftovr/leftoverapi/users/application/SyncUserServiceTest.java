package com.leftovr.leftoverapi.users.application;

import com.leftovr.leftoverapi.users.application.services.SyncUserService;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import com.leftovr.leftoverapi.users.testSupport.users.api.request.SyncUserRequestTestBuilder;
import com.leftovr.leftoverapi.users.testSupport.users.domain.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class SyncUserServiceTest {
    UserRepository repo = mock(UserRepository.class);
    SyncUserService service = new SyncUserService(repo);

    @Test
    void syncUser_nonExistingUser_createsNewUser() {
        // Arrange
        var syncUserRequest = SyncUserRequestTestBuilder.aDefault().build();
        var userId = "new-user-id";
        when(repo.findById(userId)).thenReturn(Optional.empty());

        // Act
        var result = service.syncUser(userId, syncUserRequest);

        // Assert
        assertTrue(result.created());
        verify(repo).findById(userId);
        verify(repo).save(argThat(user ->
                userId.equals(user.getId()) &&
                        syncUserRequest.email().equals(user.getEmail()) &&
                        syncUserRequest.username().equals(user.getUsername())));
    }

    @Test
    void syncUser_throwsExceptionWhenUsernameNotUnique() {
        // Arrange
        var syncUserRequest = SyncUserRequestTestBuilder.aDefault().build();
        var userId = "new-user-id";
        when(repo.findById(userId)).thenReturn(Optional.empty());
        when(repo.existsByUsername(syncUserRequest.username())).thenReturn(true);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> service.syncUser(userId, syncUserRequest));
    }

    @Test
    void syncUser_throwsExceptionWhenEmailNotUnique() {
        // Arrange
        var syncUserRequest = SyncUserRequestTestBuilder.aDefault().build();
        var userId = "new-user-id";
        when(repo.findById(userId)).thenReturn(Optional.empty());
        when(repo.existsByEmail(syncUserRequest.email())).thenReturn(true);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> service.syncUser(userId, syncUserRequest));
    }

    @Test
    void syncUser_existingUser_updatesUser() {
        // Arrange
        var syncUserRequest = SyncUserRequestTestBuilder
                .aDefault()
                .withEmail("new.email@leftovr.com")
                .withUsername("new.username")
                .build();
        var userId = "existing-user-id";
        var existingUser = UserTestBuilder
                .aDefault()
                .withEmail("old.email@leftovr.com")
                .withUsername("old.username")
                .build();

            when(repo.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        var result = service.syncUser(userId, syncUserRequest);

        // Assert
        assertFalse(result.created());
        verify(repo).findById(userId);
        assertEquals(existingUser.getEmail(), syncUserRequest.email());
        assertEquals(existingUser.getUsername(), syncUserRequest.username());
    }
}