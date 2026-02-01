package com.leftovr.leftoverapi.users.common;

import com.leftovr.leftoverapi.users.domain.exceptions.SomeAllergiesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeDietaryPreferencesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeRestrictionsNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleUserNotFoundException() {
        // Arrange
        String userId = "user-id";
        UserNotFoundException exception = new UserNotFoundException(userId);

        // act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUserNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals(exception.getMessage(), response.getBody().message());
    }

    @Test
    void testHandleSomeDietaryPreferencesNotFoundException() {
        // Arrange
        String message = "Some dietary preferences not found";
        SomeDietaryPreferencesNotFoundException exception = new SomeDietaryPreferencesNotFoundException(message);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleSomeDietaryPreferencesNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(exception.getMessage(), response.getBody().message());
    }

    @Test
    void testHandleSomeRestrictionsNotFoundException() {
        // Arrange
        String message = "Some restrictions not found";
        SomeRestrictionsNotFoundException exception = new SomeRestrictionsNotFoundException(message);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleSomeRestrictionsNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(exception.getMessage(), response.getBody().message());
    }

    @Test
    void testHandleSomeAllergiesNotFoundException() {
        // Arrange
        String message = "Some allergies not found";
        SomeAllergiesNotFoundException exception = new SomeAllergiesNotFoundException(message);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleSomeAllergiesNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(exception.getMessage(), response.getBody().message());
    }
}
