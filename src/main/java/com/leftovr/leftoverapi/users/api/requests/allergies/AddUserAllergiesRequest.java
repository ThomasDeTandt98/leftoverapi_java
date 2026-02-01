package com.leftovr.leftoverapi.users.api.requests.allergies;

import java.util.Set;
import java.util.UUID;

public record AddUserAllergiesRequest(Set<UUID> allergyIds) {
}
