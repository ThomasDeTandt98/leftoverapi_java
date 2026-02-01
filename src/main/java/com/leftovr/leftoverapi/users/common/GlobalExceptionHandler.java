package com.leftovr.leftoverapi.users.common;

import com.leftovr.leftoverapi.users.domain.exceptions.SomeAllergiesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeDietaryPreferencesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeRestrictionsNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SomeDietaryPreferencesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSomeDietaryPreferencesNotFoundException(SomeDietaryPreferencesNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SomeRestrictionsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSomeRestrictionsNotFoundException(SomeRestrictionsNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SomeAllergiesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSomeAllergiesNotFoundException(SomeAllergiesNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
