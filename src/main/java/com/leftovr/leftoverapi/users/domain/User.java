package com.leftovr.leftoverapi.users.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(unique = true, nullable = false, length = 100)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_complete", nullable = false)
    private boolean isComplete;

    @ManyToMany
    @JoinTable(
            name = "user_dietary_preferences",
            schema = "users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_preference_id")
    )
    private Set<DietaryPreference> dietaryPreferences = new HashSet<>();

    public Set<DietaryPreference> getDietaryPreferences() {
        return Set.copyOf(dietaryPreferences);
    }

    @ManyToMany
    @JoinTable(
            name = "user_restrictions",
            schema = "users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "restriction_id")
    )
    private Set<Restriction> restrictions = new HashSet<>();

    public Set<Restriction> getRestrictions() {
        return Set.copyOf(restrictions);
    }

    @ManyToMany
    @JoinTable(
            name = "user_allergies",
            schema = "users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "allergy_id")
    )
    private Set<Allergy> allergies = new HashSet<>();

    public Set<Allergy> getAllergies() {
        return Set.copyOf(allergies);
    }

    public void addDietaryPreferences(Set<DietaryPreference> dietaryPreferences) {
        this.dietaryPreferences.addAll(dietaryPreferences);
    }

    public void addRestrictions(Set<Restriction> restrictions) {
        this.restrictions.addAll(restrictions);
    }

    public void addAllergies(Set<Allergy> allergies) {
        this.allergies.addAll(allergies);
    }
}
