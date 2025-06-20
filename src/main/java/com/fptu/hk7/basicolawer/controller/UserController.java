package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Request.ChangePasswordDTO;
import com.fptu.hk7.basicolawer.dto.Request.UpdateUserDTO;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("getAll")
    public ResponseEntity <List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<User> createUser(User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.ok(createdUser);
    }
    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUserDTO userUpdateDTO) {
        User updateUser = userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok(updateUser);
    }

        @PostMapping("/change-password")
        public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
            userService.changePassword(changePasswordDTO);
            return ResponseEntity.ok("Password changed successfully");
        }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
