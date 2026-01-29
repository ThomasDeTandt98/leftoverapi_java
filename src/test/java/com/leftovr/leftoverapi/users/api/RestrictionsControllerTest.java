package com.leftovr.leftoverapi.users.api;

import com.leftovr.leftoverapi.users.api.controller.RestrictionsController;
import com.leftovr.leftoverapi.users.application.results.restrictions.RestrictionsLookupResult;
import com.leftovr.leftoverapi.users.application.services.restrictions.RestrictionsService;
import com.leftovr.leftoverapi.users.testSupport.users.TestSecurityConfig;
import com.leftovr.leftoverapi.users.testSupport.users.application.restrictions.RestrictionsLookupResultTestBuilder;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RestrictionsController.class)
@Import(TestSecurityConfig.class)
public class RestrictionsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RestrictionsService restrictionsService;

    @Test
    void getRestrictions_returns200Ok() throws Exception {
        // Arrange
        String userId = "auth0|123456";

        List<RestrictionsLookupResult> lookupItems = List.of(
                RestrictionsLookupResultTestBuilder.aDefault().build(),
                RestrictionsLookupResultTestBuilder.aDefault().build()
        );

        when(restrictionsService.getLookupItems()).thenReturn(lookupItems);

        // Act & Assert
        mockMvc.perform(get("/api/users/preferences/restrictions/lookup")
                        .with(jwt().jwt(j -> j.subject(userId)))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(lookupItems.get(0).id().toString()))
                .andExpect(jsonPath("$.[1].id").value(lookupItems.get(1).id().toString()))
                .andExpect(jsonPath("$.[0].name").value(lookupItems.get(0).name()))
                .andExpect(jsonPath("$.[1].name").value(lookupItems.get(1).name()));
    }
}
