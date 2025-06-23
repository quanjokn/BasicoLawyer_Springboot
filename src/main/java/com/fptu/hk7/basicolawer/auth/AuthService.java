package com.fptu.hk7.basicolawer.auth;

import com.fptu.hk7.basicolawer.enums.Role;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// activate new user
@Service
@RequiredArgsConstructor
public class AuthService {
    public final UserRepository userService;
    public final BCryptPasswordEncoder passwordEncoder;

    public User activateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setStatus("active");
        return userService.save(user);
    }
}
