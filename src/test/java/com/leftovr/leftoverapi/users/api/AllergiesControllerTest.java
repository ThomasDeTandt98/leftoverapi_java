package com.leftovr.leftoverapi.users.api;

import com.leftovr.leftoverapi.users.api.controller.AllergiesController;
import com.leftovr.leftoverapi.users.application.results.allergies.AllergiesLookupResult;
import com.leftovr.leftoverapi.users.application.services.allergies.AllergiesService;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeAllergiesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import com.leftovr.leftoverapi.users.testSupport.users.TestSecurityConfig;
import com.leftovr.leftoverapi.users.testSupport.users.application.allergies.AllergiesLookupResultTestBuilder;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AllergiesController.class)
@Import({TestSecurityConfig.class})
public class AllergiesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AllergiesService allergiesService;

    @Test
    void getAllergies_returns200Ok() throws Exception {
        // Arrange
        String userId = "auth|123456";

        List<AllergiesLookupResult> lookupItems = List.of(
                AllergiesLookupResultTestBuilder.aDefault().build(),
                AllergiesLookupResultTestBuilder.aDefault().build()
        );

        when(allergiesService.getLookupItems()).thenReturn(lookupItems);

        // Act & Assert
        mockMvc.perform(get("/api/users/preferences/allergies/lookup")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(lookupItems.get(0).id().toString()))
                .andExpect(jsonPath("$.[1].id").value(lookupItems.get(1).id().toString()))
                .andExpect(jsonPath("$.[0].name").value(lookupItems.get(0).name()))
                .andExpect(jsonPath("$.[1].name").value(lookupItems.get(1).name()));
    }

    @Test
    void createUserAllergies_returns404NotFound_whenUserDoesNotExist() throws Exception {
        // Arrange
        String userId = "auth|nonexistent";

        String requestBody = """
                {
                    "allergyIds": ["d290f1ee-6c54-4b01-90e6-d701748f0851", 
                    "c4b1d5f2-3c4b-4f5a-8e2d-1f2e3d4c5b6a"]
                }
                """;

        doThrow(new UserNotFoundException(userId))
                .when(allergiesService)
                .addUserAllergies(any(), any());

        // Act & Assert
        mockMvc.perform(post("/api/users/preferences/allergies")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUserAllergies_returns400BadRequest_whenAllergyDoesNotExist() throws Exception {
        // Arrange
        String userId = "auth|123456";

        String requestBody = """
                {
                    "allergyIds": ["non-existent-allergy-id"]
                }
                """;

        doThrow(new SomeAllergiesNotFoundException("Some allergies not found"))
                .when(allergiesService)
                .addUserAllergies(any(), any());

        // Act & Assert
        mockMvc.perform(post("/api/users/preferences/allergies")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserAllergies_returns200Ok() throws Exception {
        // Arrange
        String userId = "auth|123456";

        String requestBody = """
                {
                    "allergyIds": ["d290f1ee-6c54-4b01-90e6-d701748f0851", 
                    "c4b1d5f2-3c4b-4f5a-8e2d-1f2e3d4c5b6a"]
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/users/preferences/allergies")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(requestBody))
                .andExpect(status().isOk());
    }
}
