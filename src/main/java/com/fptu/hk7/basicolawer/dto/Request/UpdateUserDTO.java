package com.fptu.hk7.basicolawer.dto.Request;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String status;
}
