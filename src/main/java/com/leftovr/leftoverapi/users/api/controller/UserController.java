package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.requests.SyncUserRequest;
import com.leftovr.leftoverapi.users.api.responses.UserResponse;
import com.leftovr.leftoverapi.users.application.SyncUserResult;
import com.leftovr.leftoverapi.users.application.SyncUserService;
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

   public UserController(SyncUserService userService) {
        this.syncUserService = userService;
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
}
