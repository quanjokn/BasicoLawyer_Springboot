package com.fptu.hk7.basicolawer.dto.Request;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
