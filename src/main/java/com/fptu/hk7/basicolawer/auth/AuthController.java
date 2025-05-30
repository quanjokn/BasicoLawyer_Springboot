package com.fptu.hk7.basicolawer.auth;

import com.fptu.hk7.basicolawer.dto.ApiResponse;
import com.fptu.hk7.basicolawer.dto.LoginDTO;
import com.fptu.hk7.basicolawer.dto.jwtResponse;
import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.exception.ErrorCode;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.service.EmailService;
import com.fptu.hk7.basicolawer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final BCryptPasswordEncoder passwordEncoder;
    private String otp;
    private User user;

    private final UserService userService;
    private final EmailService emailService;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email) {
        this.user = userService.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String otp = emailService.generateOTP();
        this.otp = otp;
        emailService.sendHtmlEmail(email, emailService.subject_forgotPasswordEmail, emailService.bodyForgotPasswordEmail(otp));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Password reset link sent to your email",
                200,
                null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOTP(@RequestParam String otp) {
        if (!this.otp.equals(otp)) {
            throw new AppException(ErrorCode.ERROR_OTP);
        }
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Verify OPT successfully",
                200,
                null));
    }
    @PostMapping("reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam String newPassword) {
        try {
            this.user.setPassword(passwordEncoder.encode(newPassword));
            userService.save(this.user);
        }catch (Exception e) {
            throw new AppException(ErrorCode.UNABLE_TO_CHANGE_PASSWORD);
        }
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Password reset successfully",
                200,
                null));
    }
}
