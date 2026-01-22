package com.leftovr.leftoverapi.users.application.results.users;

import com.leftovr.leftoverapi.users.domain.User;

public record CompleteUserProfileResult(User user, boolean updated) {
}