package com.leftovr.leftoverapi.users.api;

import com.leftovr.leftoverapi.users.api.controller.UserController;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SyncUserService service;

    @Test
    void returns200Ok() throws Exception {
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
        when(service.syncUser(userId, syncUserRequest)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
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
