package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.User;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(UUID id);
    List<User> getAllUsers();
    User updateUser(UUID id, User user);
    void deleteUser(UUID id);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
