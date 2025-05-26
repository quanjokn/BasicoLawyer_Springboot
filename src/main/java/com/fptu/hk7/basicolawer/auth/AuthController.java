package com.fptu.hk7.basicolawer.auth;

import com.fptu.hk7.basicolawer.dto.ApiResponse;
import com.fptu.hk7.basicolawer.dto.LoginDTO;
import com.fptu.hk7.basicolawer.dto.jwtResponse;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody User user) throws RuntimeException{
        return ResponseEntity.ok(userService.register(user));
    }
}
