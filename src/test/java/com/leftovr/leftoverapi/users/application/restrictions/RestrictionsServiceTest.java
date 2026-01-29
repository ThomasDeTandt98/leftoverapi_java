package com.leftovr.leftoverapi.users.application.restrictions;

import com.leftovr.leftoverapi.users.application.results.restrictions.RestrictionsLookupResult;
import com.leftovr.leftoverapi.users.application.services.restrictions.RestrictionsService;
import com.leftovr.leftoverapi.users.domain.Restriction;
import com.leftovr.leftoverapi.users.infrastructure.RestrictionsRepository;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import com.leftovr.leftoverapi.users.testSupport.users.domain.RestrictionTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestrictionsServiceTest {

    RestrictionsRepository restrictionsRepository= mock(RestrictionsRepository.class);
    UserRepository userRepository= mock(UserRepository.class);
    RestrictionsService restrictionsService = new RestrictionsService(restrictionsRepository, userRepository);

    @Test
    void getRestrictions_shouldReturnRestrictions() {
        // Arrange
        List<Restriction> restrictions = List.of(
                RestrictionTestBuilder.aDefault().build(),
                RestrictionTestBuilder.aDefault().withName("No Nuts").build()
        );

        when(restrictionsRepository.findAllByIsActiveTrue()).thenReturn(restrictions);

        // Act
        List<RestrictionsLookupResult> result = restrictionsService.getLookupItems();

        // Assert
        assertEquals(result.size(), restrictions.size());

        assertEquals(restrictions.getFirst().getName(), result.getFirst().name());
        assertEquals(restrictions.getFirst().getId(), result.getFirst().id());

        assertEquals(restrictions.get(1).getName(), result.get(1).name());
        assertEquals(restrictions.get(1).getId(), result.get(1).id());
    }

    @Test
    void getRestrictions_shouldReturnEmptyList() {
        // Arrange
        when(restrictionsRepository.findAllByIsActiveTrue()).thenReturn(new ArrayList<>());

        // Act
        List<RestrictionsLookupResult> result = restrictionsService.getLookupItems();

        // Assert
        assertEquals(0, result.size());
    }
}
