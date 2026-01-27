package com.leftovr.leftoverapi.users.api;

import com.leftovr.leftoverapi.users.api.controller.DietaryPreferencesController;
import com.leftovr.leftoverapi.users.application.results.dietaryPreferences.DietaryPreferencesLookupResult;
import com.leftovr.leftoverapi.users.application.services.dietaryPreferences.DietaryPreferencesService;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeDietaryPreferencesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import com.leftovr.leftoverapi.users.testSupport.users.TestSecurityConfig;
import com.leftovr.leftoverapi.users.testSupport.users.application.dietaryPreferences.DietaryPreferencesLookupResultTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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

@WebMvcTest(controllers = DietaryPreferencesController.class)
@Import(TestSecurityConfig.class)
public class DietaryPreferencesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    DietaryPreferencesService dietaryPreferencesService;

    @Test
    void getDietaryPreferences_returns200Ok() throws Exception {
        // Arrange
        String userId = "auth0|1234567890";

        List<DietaryPreferencesLookupResult> lookups = List.of(
                DietaryPreferencesLookupResultTestBuilder.aDefault().withName("Vegan").build(),
                DietaryPreferencesLookupResultTestBuilder.aDefault().withName("Pescatarian").build()
        );

        when(dietaryPreferencesService.getLookupItems()).thenReturn(lookups);

        // Act & Assert
        mockMvc.perform(get("/api/users/preferences/dietary/lookup")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Vegan"))
                .andExpect(jsonPath("$.[1].name").value("Pescatarian"));
    }

    @Test
    void createDietaryPreferences_returns200Ok() throws Exception {
        // Arrange
        String userId = "auth0|1234567890";
        String requestBody = """
                {
                    "dietaryPreferenceIds": [
                        "a2b44b8a-7fa4-478b-8fbe-392e274bba08"
                    ]
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/users/preferences/dietary")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void createDietaryPreferences_userDoesNotExist_returns404NotFound() throws Exception{
        // Arrange
        String userId = "auth0|non-existing-user";
        String requestBody = """
                {
                    "dietaryPreferenceIds": [
                        "a2b44b8a-7fa4-478b-8fbe-392e274bba08"
                    ]
                }
                """;

        doThrow(new UserNotFoundException(userId))
                .when(dietaryPreferencesService)
                .addUserDietaryPreferences(any(), any());

        // Act & Assert
        mockMvc.perform(post("/api/users/preferences/dietary")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void addDietaryPreferences_dietaryPreferencesDoesNotExist_returns403BadRequest() throws Exception {
        // Arrange
        String userId = "auth0|1234567890";

        doThrow(new SomeDietaryPreferencesNotFoundException(userId))
                .when(dietaryPreferencesService)
                .addUserDietaryPreferences(any(), any());

        // Act & Assert
        mockMvc.perform(post("/api/users/preferences/dietary")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
