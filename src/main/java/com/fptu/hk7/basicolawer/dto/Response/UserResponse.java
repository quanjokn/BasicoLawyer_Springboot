package com.fptu.hk7.basicolawer.dto.Response;

import com.fptu.hk7.basicolawer.pojo.Enum.Role;
import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String accountname;
    private String email;
    private String phone;
    private String address;
    private Role role;
    private String token;
}
