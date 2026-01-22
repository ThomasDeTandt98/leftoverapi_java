package com.leftovr.leftoverapi.users.api;

import com.leftovr.leftoverapi.users.api.controller.DietaryPreferencesController;
import com.leftovr.leftoverapi.users.application.results.dietaryPreferences.DietaryPreferencesLookupResult;
import com.leftovr.leftoverapi.users.application.services.dietaryPreferences.DietaryPreferencesService;
import com.leftovr.leftoverapi.users.testSupport.users.TestSecurityConfig;
import com.leftovr.leftoverapi.users.testSupport.users.application.dietaryPeferences.DietaryPreferencesLookupResultTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mockMvc.perform(get("/api/users/dietary-preferences/lookup")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Vegan"))
                .andExpect(jsonPath("$.[1].name").value("Pescatarian"));
    }
}
