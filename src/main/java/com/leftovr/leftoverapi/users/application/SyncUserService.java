package com.leftovr.leftoverapi.users.application;

import com.leftovr.leftoverapi.users.api.requests.SyncUserRequest;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SyncUserService {
    private final UserRepository userRepository;

    public SyncUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User syncUser(String id, SyncUserRequest syncUserRequest) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setEmail(syncUserRequest.email().trim());
                    existingUser.setUsername(syncUserRequest.username().trim());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    var newUser = new User();
                    newUser.setId(id);
                    newUser.setEmail(syncUserRequest.email().trim());
                    newUser.setUsername(syncUserRequest.username().trim());
                    return userRepository.save(newUser);
                });
    }
}
