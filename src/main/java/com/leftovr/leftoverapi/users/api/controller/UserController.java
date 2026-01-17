package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.requests.SyncUserRequest;
import com.leftovr.leftoverapi.users.application.SyncUserService;
import com.leftovr.leftoverapi.users.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final SyncUserService syncUserService;

   public UserController(SyncUserService userService) {
        this.syncUserService = userService;
    }

    @PostMapping("/{id}")
    public String syncUser(
            @PathVariable @NotBlank @Length(max = 100) String id,
            @Valid @RequestBody SyncUserRequest syncUserRequest) {
        User user = syncUserService.syncUser(id, syncUserRequest);
        return user.getId();
    }
}
