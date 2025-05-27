package com.fptu.hk7.basicolawer.auth;

import com.fptu.hk7.basicolawer.dto.ApiResponse;
import com.fptu.hk7.basicolawer.dto.LoginDTO;
import com.fptu.hk7.basicolawer.dto.jwtResponse;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<jwtResponse>> login(@RequestBody LoginDTO loginDTO) throws AppException {
        return ResponseEntity.ok(userService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody User user) throws RuntimeException{
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> profile() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "User profile retrieved successfully",
                200,
                user));
    }
}
