package com.fptu.hk7.basicolawer.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotBlank(message = "htmlContent can not be blank")
    private String htmlContent;
}
