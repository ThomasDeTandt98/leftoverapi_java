package com.leftovr.leftoverapi.users.infrastructure;

import com.leftovr.leftoverapi.users.domain.DietaryPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DietaryPreferencesRepository extends JpaRepository<DietaryPreference, UUID> {
    List<DietaryPreference> findAllByIsActiveTrue();
}
