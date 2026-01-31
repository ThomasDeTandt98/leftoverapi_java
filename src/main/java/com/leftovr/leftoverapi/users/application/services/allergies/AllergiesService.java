package com.leftovr.leftoverapi.users.application.services.allergies;

import com.leftovr.leftoverapi.users.application.results.allergies.AllergiesLookupResult;
import com.leftovr.leftoverapi.users.infrastructure.AllergiesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergiesService {

    private final AllergiesRepository allergiesRepository;

    public AllergiesService(AllergiesRepository allergiesRepository) {
        this.allergiesRepository = allergiesRepository;
    }

    public List<AllergiesLookupResult> getLookupItems() {
        return allergiesRepository.findAllByIsActiveIsTrue()
                .stream()
                .map(allergy -> new AllergiesLookupResult(allergy.getId(), allergy.getName()))
                .toList();
    }
}
