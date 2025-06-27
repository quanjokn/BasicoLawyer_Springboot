package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicoeventdata.event.UserEvent;
import com.fptu.hk7.basicolawer.auth.AuthService;
import com.fptu.hk7.basicolawer.config.JWT.JWTUtil;
import com.fptu.hk7.basicolawer.dto.Request.ChangePasswordDTO;
import com.fptu.hk7.basicolawer.dto.Request.RegisterDTO;
import com.fptu.hk7.basicolawer.dto.Request.UpdateUserDTO;
import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import com.fptu.hk7.basicolawer.dto.Request.LoginDTO;
import com.fptu.hk7.basicolawer.dto.Response.jwtResponse;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.exception.ErrorCode;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.producer.UserProducer;
import com.fptu.hk7.basicolawer.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserProducer userProducer;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDetailsService userDetailsService, AuthService authService, AuthenticationManager authenticationManager, JWTUtil jwtUtil, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.userProducer = userProducer;
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UpdateUserDTO updateUserDTO) {
        User user = getCurrentUser();
        user.setFullname(updateUserDTO.getFullname());
        user.setUsername(updateUserDTO.getUsername());
        user.setPhone(updateUserDTO.getPhone());
        user.setEmail(updateUserDTO.getEmail());
        user.setAddress(updateUserDTO.getAddress());
        user = userRepository.save(user);
        // sync data
        UserEvent userEvent = modelMapper.map(user, UserEvent.class);
        userEvent.setUser_id(user.getId());
        userEvent.setRole(user.getRole().toString());
        userProducer.produce(userEvent, "user-update");

        return user;
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
        // sync data
        UserEvent userEvent = new UserEvent();
        userEvent.setUser_id(id);
        userProducer.produce(userEvent, "user-delete");
        //
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ApiResponse<jwtResponse> login(LoginDTO loginDTO) throws AppException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            return new ApiResponse<>(
                    true,
                    "Login successful",
                    200,
                    new jwtResponse(token));
        } catch (AuthenticationException e) {
            throw new AppException(ErrorCode.LOGIN_FAIL);
        }
    }

    @Override
    public ApiResponse<Void> register(RegisterDTO registerDTO) {
        User user = new User(
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getFullname(),
                registerDTO.getPhone(),
                registerDTO.getEmail(),
                registerDTO.getStatus(),
                registerDTO.getRole());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        user = authService.activateUser(user);

        // sync data
        UserEvent userEvent = modelMapper.map(user, UserEvent.class);
        userEvent.setUser_id(user.getId());
        userEvent.setRole(registerDTO.getRole().toString());
        userProducer.produce(userEvent, "user-create");
        //


        return new ApiResponse<>(
                true,
                "User registered successfully",
                200,
                null);
    }

    @Override
    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public void forgotPassword(String email) {

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getAllLawyers() {
        return List.of();
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        {
            User user = getCurrentUser();

            if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.INCORRECT_PASSWORD);
            }
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(user);
        }
    }
}
