package com.leftovr.leftoverapi.users.infrastructure;

import com.leftovr.leftoverapi.users.domain.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AllergiesRepository extends JpaRepository<Allergy, UUID> {
    List<Allergy> findAllByIsActiveIsTrue();
}
