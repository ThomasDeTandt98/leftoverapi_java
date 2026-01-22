package com.leftovr.leftoverapi.users.application.results.users;

import com.leftovr.leftoverapi.users.domain.User;

public record SyncUserResult(User user, boolean created) {}