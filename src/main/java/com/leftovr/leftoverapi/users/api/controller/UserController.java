package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.requests.CompleteUserProfileRequest;
import com.leftovr.leftoverapi.users.api.requests.SyncUserRequest;
import com.leftovr.leftoverapi.users.api.responses.UserResponse;
import com.leftovr.leftoverapi.users.application.results.CompleteUserProfileResult;
import com.leftovr.leftoverapi.users.application.results.SyncUserResult;
import com.leftovr.leftoverapi.users.application.services.CompleteUserProfileService;
import com.leftovr.leftoverapi.users.application.services.SyncUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final SyncUserService syncUserService;
    private final CompleteUserProfileService completeUserProfileService;

    public UserController(
            SyncUserService userService,
            CompleteUserProfileService completeUserProfileService) {
        this.syncUserService = userService;
        this.completeUserProfileService = completeUserProfileService;
    }

    @PostMapping()
    public ResponseEntity<UserResponse> syncUser(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody SyncUserRequest syncUserRequest) {
        String userId = jwt.getSubject();

        SyncUserResult result = syncUserService.syncUser(userId, syncUserRequest);
        UserResponse response = UserResponse.fromUser(result.user());

        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/complete")
    public ResponseEntity<UserResponse> completeUserProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CompleteUserProfileRequest request) {
        String userId = jwt.getSubject();

        CompleteUserProfileResult result = completeUserProfileService.completeUserProfile(userId, request);
        UserResponse response = UserResponse.fromUser(result.user());

        return ResponseEntity.ok(response);
    }
}
