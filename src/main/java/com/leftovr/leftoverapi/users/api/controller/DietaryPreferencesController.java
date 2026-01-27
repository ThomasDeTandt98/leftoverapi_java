package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.requests.dietaryPreferences.AddUserDietaryPreferencesRequest;
import com.leftovr.leftoverapi.users.api.responses.dietaryPreferences.DietaryPreferencesLookupResponse;
import com.leftovr.leftoverapi.users.application.results.dietaryPreferences.DietaryPreferencesLookupResult;
import com.leftovr.leftoverapi.users.application.services.dietaryPreferences.DietaryPreferencesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users/preferences/dietary")
public class DietaryPreferencesController {

    private final DietaryPreferencesService dietaryPreferencesService;

    public DietaryPreferencesController(DietaryPreferencesService dietaryPreferencesService) {
        this.dietaryPreferencesService = dietaryPreferencesService;
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<DietaryPreferencesLookupResponse>> getDietaryPreferences() {
        List<DietaryPreferencesLookupResult> result = dietaryPreferencesService.getLookupItems();
        List<DietaryPreferencesLookupResponse> response = result.stream()
                .map(dp -> new DietaryPreferencesLookupResponse(dp.id(), dp.name()))
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<Void> createUserDietaryPreferences(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid AddUserDietaryPreferencesRequest request
    ) {
        String userId = jwt.getSubject();
        dietaryPreferencesService.addUserDietaryPreferences(userId, request.dietaryPreferenceIds());
        return ResponseEntity.ok().build();
    }
}
