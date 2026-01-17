package com.leftovr.leftoverapi.users.api;

import com.leftovr.leftoverapi.users.api.controller.UserController;
import com.leftovr.leftoverapi.users.api.requests.CompleteUserProfileRequest;
import com.leftovr.leftoverapi.users.application.results.CompleteUserProfileResult;
import com.leftovr.leftoverapi.users.application.results.SyncUserResult;
import com.leftovr.leftoverapi.users.application.services.CompleteUserProfileService;
import com.leftovr.leftoverapi.users.application.services.SyncUserService;
import com.leftovr.leftoverapi.users.testSupport.users.api.request.SyncUserRequestTestBuilder;
import com.leftovr.leftoverapi.users.testSupport.users.domain.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SyncUserService syncUserService;
    @MockitoBean
    CompleteUserProfileService completeUserProfileService;

    @Test
    void syncUser_newUser_returns201Created() throws Exception {
        // Arrange
        var userId = "test-user-id";
        var syncUserRequest = SyncUserRequestTestBuilder.aDefault().build();
        var json = """
                {
                    "email": "%s",
                    "username": "%s"
                }
                """.formatted(syncUserRequest.email(), syncUserRequest.username());

        var user = UserTestBuilder.aDefault().build();
        var result = new SyncUserResult(user, true);
        when(syncUserService.syncUser(userId, syncUserRequest)).thenReturn(result);

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void syncUser_existingUser_returns200Ok() throws Exception {
        // Arrange
        var userId = "test-user-id";
        var syncUserRequest = SyncUserRequestTestBuilder.aDefault().build();
        var json = """
                {
                    "email": "%s",
                    "username": "%s"
                }
                """.formatted(syncUserRequest.email(), syncUserRequest.username());

        var user = UserTestBuilder.aDefault().build();
        var result = new SyncUserResult(user, false);
        when(syncUserService.syncUser(userId, syncUserRequest)).thenReturn(result);

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void syncUser_withInvalidRequest_returns400BadRequest() throws Exception {
        // Arrange
        var userId = "test-user-id";
        var json = """
                {
                    "email": "invalid-email-format",
                    "username": ""
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void syncUser_withTooLongUserId_returns400BadRequest() throws Exception {
        // Arrange
        var userId = "a".repeat(101);
        var syncUserRequest = SyncUserRequestTestBuilder.aDefault().build();
        var json = """
                {
                    "email": "%s",
                    "username": "%s"
                }
                """.formatted(syncUserRequest.email(), syncUserRequest.username());

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void completeUserProfile_nonExistingUser_returns404NotFound() throws Exception {
        // Arrange
        var userId = "non-existing-user-id";
        var json = """
                {
                    "firstName": "John",
                    "lastName": "Doe"
                }
                """;

        when(completeUserProfileService.completeUserProfile(any(String.class), any(CompleteUserProfileRequest.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        // Act & Assert
        mockMvc.perform(put("/api/users/{userId}/complete", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void completeUserProfile_validationFails_returns400BadRequest() throws Exception {
        // Arrange
        var userId = "test-user-id";
        var json = """
                {
                    "firstName": "",
                    "lastName": ""
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/api/users/{userId}/complete", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void completeUserProfile_existingUser_returns200Ok() throws Exception {
        // Arrange
        var userId = "test-user-id";
        var json = """
                {
                    "firstName": "John",
                    "lastName": "Doe"
                }
                """;

        var user = UserTestBuilder.aDefault()
                .withFirstName("John")
                .withLastName("Doe")
                .build();

        when(completeUserProfileService.completeUserProfile(any(String.class), any(CompleteUserProfileRequest.class)))
                .thenReturn(new CompleteUserProfileResult(user, true));

        // Act & Assert
        mockMvc.perform(put("/api/users/{userId}/complete", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
}
