package com.leftovr.leftoverapi.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "allergies", schema = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Allergy {

    @Id
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
