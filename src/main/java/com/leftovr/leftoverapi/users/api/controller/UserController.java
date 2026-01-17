package com.leftovr.leftoverapi.users.api.controller;

import com.leftovr.leftoverapi.users.api.requests.SyncUserRequest;
import com.leftovr.leftoverapi.users.application.UserService;
import com.leftovr.leftoverapi.users.domain.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

   public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{id}")
    public String syncUser(
            @PathVariable String id,
            @Valid @RequestBody SyncUserRequest syncUserRequest) {
        User user = userService.syncUser(id, syncUserRequest);
        return user.getId();
    }
}
