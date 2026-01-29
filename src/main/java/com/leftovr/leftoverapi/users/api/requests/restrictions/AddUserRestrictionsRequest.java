package com.leftovr.leftoverapi.users.api.requests.restrictions;

import java.util.Set;
import java.util.UUID;

public record AddUserRestrictionsRequest(Set<UUID> restrictionIds) { }
