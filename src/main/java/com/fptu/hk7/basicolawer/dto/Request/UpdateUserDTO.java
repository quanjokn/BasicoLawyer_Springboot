package com.fptu.hk7.basicolawer.dto.Request;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String username;
    private String email;
    private String phone;
    private String fullname;
}
