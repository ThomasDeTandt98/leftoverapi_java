package com.leftovr.leftoverapi.users.api;

import com.leftovr.leftoverapi.users.api.controller.UserController;
import com.leftovr.leftoverapi.users.application.SyncUserResult;
import com.leftovr.leftoverapi.users.application.SyncUserService;
import com.leftovr.leftoverapi.users.testSupport.users.application.requests.SyncUserRequestTestBuilder;
import com.leftovr.leftoverapi.users.testSupport.users.domain.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SyncUserService service;

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
        when(service.syncUser(userId, syncUserRequest)).thenReturn(result);

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
        when(service.syncUser(userId, syncUserRequest)).thenReturn(result);

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
}
