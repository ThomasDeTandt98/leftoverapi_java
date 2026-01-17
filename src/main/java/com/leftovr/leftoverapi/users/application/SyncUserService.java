package com.leftovr.leftoverapi.users.application;

import com.leftovr.leftoverapi.users.api.requests.SyncUserRequest;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class SyncUserService {
    private final UserRepository userRepository;

    public SyncUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User syncUser(String id, SyncUserRequest syncUserRequest) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            validateInputParameters(syncUserRequest);

            User newUser = new User();
            newUser.setId(id);
            newUser.setEmail(syncUserRequest.email());
            newUser.setUsername(syncUserRequest.username());
            return userRepository.save(newUser);
        } else {
            user.setEmail(syncUserRequest.email());
            user.setUsername(syncUserRequest.username());
            return user;
        }
    }

    private void validateInputParameters(SyncUserRequest syncUserRequest) {
        String email = syncUserRequest.email();
        String username = syncUserRequest.username();
        validateEmailUniqueness(email);
        validateUsernameUniqueness(username);
    }

    private void validateEmailUniqueness(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use: " + email);
        }
    }

    private void validateUsernameUniqueness(String username) {
        boolean usernameExists = userRepository.existsByUsername(username);
        if (usernameExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use: " + username);
        }
    }
}
