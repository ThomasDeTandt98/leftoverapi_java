package com.leftovr.leftoverapi.users.application;

import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import com.leftovr.leftoverapi.users.testSupport.users.application.requests.SyncUserRequestTestBuilder;
import com.leftovr.leftoverapi.users.testSupport.users.domain.UserTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
        service.syncUser(userId, syncUserRequest);

        // Assert
        verify(repo).findById(userId);
        verify(repo).save(argThat(user ->
                userId.equals(user.getId()) &&
                        syncUserRequest.email().equals(user.getEmail()) &&
                        syncUserRequest.username().equals(user.getUsername())));
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
                .aUser()
                .withEmail("old.email@leftovr.com")
                .withUsername("old.username")
                .build();

        when(repo.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        service.syncUser(userId, syncUserRequest);

        // Assert
        verify(repo).findById(userId);
        verify(repo).save(argThat(user ->
                userId.equals(user.getId()) &&
                        syncUserRequest.email().equals(user.getEmail()) &&
                        syncUserRequest.username().equals(user.getUsername())));
    }
}