package com.fptu.hk7.basicolawer.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserFormRequest {
    private UUID templateId;
    private String formData;
}