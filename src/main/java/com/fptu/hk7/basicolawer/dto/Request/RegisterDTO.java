package com.fptu.hk7.basicolawer.dto.Request;

import com.fptu.hk7.basicolawer.pojo.Enum.Role;
import com.fptu.hk7.basicolawer.validation.ValidPhone;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullname;

    @ValidPhone
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(unique = true)
    private String email;

    private String status = "active";

    private Role role = Role.CUSTOMER;
}
