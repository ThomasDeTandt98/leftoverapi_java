package com.leftovr.leftoverapi.users.application.services.restrictions;

import com.leftovr.leftoverapi.users.application.results.restrictions.RestrictionsLookupResult;
import com.leftovr.leftoverapi.users.infrastructure.RestrictionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestrictionsService {

    private final RestrictionsRepository restrictionsRepository;

    public RestrictionsService(RestrictionsRepository restrictionsRepository) {
        this.restrictionsRepository = restrictionsRepository;
    }

    public List<RestrictionsLookupResult> getLookupItems() {
        return restrictionsRepository.findAllByIsActiveTrue()
                .stream()
                .map(r -> new RestrictionsLookupResult(r.getId(), r.getName()))
                .toList();
    }
}
