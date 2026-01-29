package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.responses.restrictions.RestrictionsLookupResponse;
import com.leftovr.leftoverapi.users.application.results.restrictions.RestrictionsLookupResult;
import com.leftovr.leftoverapi.users.application.services.restrictions.RestrictionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users/preferences/restrictions")
public class RestrictionsController {

    private final RestrictionsService restrictionsService;

    public RestrictionsController(RestrictionsService restrictionsService) {
        this.restrictionsService = restrictionsService;
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<RestrictionsLookupResponse>> getRestrictions() {
        List<RestrictionsLookupResult> response = restrictionsService.getLookupItems();
        List<RestrictionsLookupResponse> result = response.stream()
                .map(r -> new RestrictionsLookupResponse(r.id(), r.name()))
                .toList();

        return ResponseEntity.ok(result);
    }
}
