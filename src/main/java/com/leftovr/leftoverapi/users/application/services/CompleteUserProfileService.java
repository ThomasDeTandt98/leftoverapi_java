package com.leftovr.leftoverapi.users.application.services;

import com.leftovr.leftoverapi.users.api.requests.CompleteUserProfileRequest;
import com.leftovr.leftoverapi.users.application.results.CompleteUserProfileResult;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class CompleteUserProfileService {

    private final UserRepository userRepository;

    public CompleteUserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CompleteUserProfileResult completeUserProfile(String id, CompleteUserProfileRequest request) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }

        User user = optionalUser.get();
        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());

        return new CompleteUserProfileResult(user, true);
    }
}
