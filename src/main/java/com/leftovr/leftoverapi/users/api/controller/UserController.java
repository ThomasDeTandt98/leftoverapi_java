package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.requests.CompleteUserProfileRequest;
import com.leftovr.leftoverapi.users.api.requests.SyncUserRequest;
import com.leftovr.leftoverapi.users.api.responses.UserResponse;
import com.leftovr.leftoverapi.users.application.results.CompleteUserProfileResult;
import com.leftovr.leftoverapi.users.application.results.SyncUserResult;
import com.leftovr.leftoverapi.users.application.services.CompleteUserProfileService;
import com.leftovr.leftoverapi.users.application.services.SyncUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final SyncUserService syncUserService;
    private final CompleteUserProfileService completeUserProfileService;

   public UserController(
           SyncUserService userService,
           CompleteUserProfileService completeUserProfileService) {
        this.syncUserService = userService;
        this.completeUserProfileService = completeUserProfileService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponse> syncUser(
            @PathVariable @NotBlank @Length(max = 100) String id,
            @Valid @RequestBody SyncUserRequest syncUserRequest) {
        SyncUserResult result = syncUserService.syncUser(id, syncUserRequest);
        UserResponse response = UserResponse.fromUser(result.user());
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<UserResponse> completeUserProfile(
            @PathVariable @NotBlank @Length(max = 100) String id,
            @Valid @RequestBody CompleteUserProfileRequest request) {
        CompleteUserProfileResult result = completeUserProfileService.completeUserProfile(id, request);
        UserResponse response = UserResponse.fromUser(result.user());
        return ResponseEntity.ok(response);
    }
}
