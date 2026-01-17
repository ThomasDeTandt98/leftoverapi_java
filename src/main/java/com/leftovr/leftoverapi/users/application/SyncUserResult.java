package com.leftovr.leftoverapi.users.application;

import com.leftovr.leftoverapi.users.domain.User;

public record SyncUserResult(User user, boolean created) {
}
