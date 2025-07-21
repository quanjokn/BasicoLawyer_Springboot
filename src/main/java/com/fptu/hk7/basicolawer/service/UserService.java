package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.*;
import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import com.fptu.hk7.basicolawer.dto.Response.jwtResponse;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.pojo.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void createUser(User user);
    Optional<User> getUserById(UUID id);
    List<User> getAllUsers();
    User updateUser(UpdateUserDTO updateUserDTO);
    void deleteUser(UUID id);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    ApiResponse<jwtResponse> login(LoginDTO loginDTO) throws AppException;

    User register(RegisterDTO registerDTO);

    ApiResponse<Void> registerlawyer(LawyerRegisterDTO lawyerRegisterDTO);

    User   getCurrentUser();

    void forgotPassword(String email);

    Optional<User> findByEmail(String email);

    User save(User user);

    List<User> getAllLawyers();

    void changePassword(ChangePasswordDTO changePasswordDTO) ;
}
