package com.leftovr.leftoverapi.users.infrastructure;

import com.leftovr.leftoverapi.users.domain.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestrictionsRepository extends JpaRepository<Restriction, UUID> {
    List<Restriction> findAllByIsActiveTrue();
}
