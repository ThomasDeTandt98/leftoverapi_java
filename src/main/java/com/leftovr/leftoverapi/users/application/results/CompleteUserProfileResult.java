package com.leftovr.leftoverapi.users.application.results;

import com.leftovr.leftoverapi.users.domain.User;

public record CompleteUserProfileResult(User user, boolean updated) {
}