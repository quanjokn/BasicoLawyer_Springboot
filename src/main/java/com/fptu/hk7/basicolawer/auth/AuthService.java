package com.fptu.hk7.basicolawer.auth;

import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// activate new user
@Service
@RequiredArgsConstructor
public class AuthService {
    public final UserService userService;
    public final BCryptPasswordEncoder passwordEncoder;

    public void activateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("STUDENT");
        user.setStatus("active");
        userService.createUser(user);
    }
}
