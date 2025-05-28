package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.ApiResponse;
import com.fptu.hk7.basicolawer.dto.LoginDTO;
import com.fptu.hk7.basicolawer.dto.jwtResponse;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.pojo.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void createUser(User user);
    Optional<User> getUserById(UUID id);
    List<User> getAllUsers();
    User updateUser(UUID id, User user);
    void deleteUser(UUID id);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

//     Authenticate user and return JWT response
    ApiResponse<jwtResponse> login(LoginDTO loginDTO) throws AppException;
    ApiResponse<Void> register(User user);

    User getCurrentUser();
}
