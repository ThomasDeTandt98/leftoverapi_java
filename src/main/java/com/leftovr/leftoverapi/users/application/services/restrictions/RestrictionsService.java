package com.leftovr.leftoverapi.users.application.services.restrictions;

import com.leftovr.leftoverapi.users.application.results.restrictions.RestrictionsLookupResult;
import com.leftovr.leftoverapi.users.domain.Restriction;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeRestrictionsNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import com.leftovr.leftoverapi.users.infrastructure.RestrictionsRepository;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RestrictionsService {

    private final RestrictionsRepository restrictionsRepository;
    private final UserRepository userRepository;

    public RestrictionsService(RestrictionsRepository restrictionsRepository, UserRepository userRepository) {
        this.restrictionsRepository = restrictionsRepository;
        this.userRepository = userRepository;
    }

    public List<RestrictionsLookupResult> getLookupItems() {
        return restrictionsRepository.findAllByIsActiveTrue()
                .stream()
                .map(r -> new RestrictionsLookupResult(r.getId(), r.getName()))
                .toList();
    }

    @Transactional
    public void addUserRestrictions(String userId, Set<UUID> restrictionIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        HashSet<Restriction> restrictions = new HashSet<>();

        restrictionIds.stream()
                .map(id -> restrictionsRepository.findByIdAndIsActiveTrue(id)
                        .orElseThrow(() -> new SomeRestrictionsNotFoundException("Some restrictions not found")))
                .forEach(restrictions::add);

        user.addRestrictions(restrictions);
    }
}
