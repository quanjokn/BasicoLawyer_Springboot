package com.fptu.hk7.basicolawer.dto.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fptu.hk7.basicolawer.validation.ValidPhone;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {
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

    private String status;
}
