package com.leftovr.leftoverapi.users.application.users;


import com.leftovr.leftoverapi.users.application.results.users.CompleteUserProfileResult;
import com.leftovr.leftoverapi.users.application.services.users.CompleteUserProfileService;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import com.leftovr.leftoverapi.users.testSupport.users.api.request.CompleteUserProfileRequestTestBuilder;
import com.leftovr.leftoverapi.users.testSupport.users.domain.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompleteUserProfileServiceTest {
    UserRepository userRepository = mock(UserRepository.class);
    CompleteUserProfileService completeUserProfileService = new CompleteUserProfileService(userRepository);

    @Test
    void completeUserProfile_throwsExceptionWhenUserNotFound() {
        // Arrange
        String nonExistingUserId = "non-existing-user-id";
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () ->
                completeUserProfileService.completeUserProfile(nonExistingUserId, null)
        );

        verify(userRepository, times(1)).findById(nonExistingUserId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void completeUserProfile_shouldUpdateUserProfile_whenUserExists() {
        // Arrange
        String firstName = "Jane";
        User user = UserTestBuilder.aDefault().build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        var request = CompleteUserProfileRequestTestBuilder
                .aDefault()
                .withFirstName(firstName)
                .withLastName("newLastName")
                .build();

        // Act
        CompleteUserProfileResult result = completeUserProfileService.completeUserProfile(user.getId(), request);

        // Assert
        verify(userRepository, times(1)).findById(user.getId());
        assertTrue(result.updated());
        assertEquals(user.getFirstName(), request.firstName());
        assertEquals(user.getLastName(), request.lastName());
        assertTrue(user.isComplete());
    }
}
