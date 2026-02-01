package com.leftovr.leftoverapi.users.application.services.allergies;

import com.leftovr.leftoverapi.users.application.results.allergies.AllergiesLookupResult;
import com.leftovr.leftoverapi.users.domain.Allergy;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeAllergiesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import com.leftovr.leftoverapi.users.infrastructure.AllergiesRepository;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AllergiesService {

    private final AllergiesRepository allergiesRepository;
    private final UserRepository userRepository;

    public AllergiesService(AllergiesRepository allergiesRepository, UserRepository userRepository) {
        this.allergiesRepository = allergiesRepository;
        this.userRepository = userRepository;
    }

    public List<AllergiesLookupResult> getLookupItems() {
        return allergiesRepository.findAllByIsActiveIsTrue()
                .stream()
                .map(allergy -> new AllergiesLookupResult(allergy.getId(), allergy.getName()))
                .toList();
    }

    @Transactional
    public void addUserAllergies(String userId, Set<UUID> allergyIds) {
       User user = userRepository.findById(userId).orElseThrow(() ->
               new UserNotFoundException(userId));

        HashSet<Allergy> allergies = new HashSet<>();

        allergyIds.stream()
                .map(id -> allergiesRepository.findByIdAndIsActiveIsTrue(id)
                        .orElseThrow(() -> new SomeAllergiesNotFoundException("Some allergies not found")))
                .forEach(allergies::add);

        user.addAllergies(allergies);
    }
}
