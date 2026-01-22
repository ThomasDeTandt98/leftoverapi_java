package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.responses.dietaryPreferences.DietaryPreferencesLookupResponse;
import com.leftovr.leftoverapi.users.application.results.dietaryPreferences.DietaryPreferencesLookupResult;
import com.leftovr.leftoverapi.users.application.services.dietaryPreferences.DietaryPreferencesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users/dietary-preferences")
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
}
