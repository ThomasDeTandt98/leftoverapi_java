package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.responses.allergies.AllergiesLookupResponse;
import com.leftovr.leftoverapi.users.application.results.allergies.AllergiesLookupResult;
import com.leftovr.leftoverapi.users.application.services.allergies.AllergiesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users/preferences/allergies")
public class AllergiesController {

    private final AllergiesService allergiesService;

    public AllergiesController(AllergiesService allergiesService) {
        this.allergiesService = allergiesService;
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<AllergiesLookupResponse>> getAllergies() {
        List<AllergiesLookupResult> result = allergiesService.getLookupItems();
        List<AllergiesLookupResponse> response = result.stream()
                .map(r -> new AllergiesLookupResponse(r.id(), r.name()))
                .toList();

        return ResponseEntity.ok(response);
    }
}
