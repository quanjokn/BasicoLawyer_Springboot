package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.FormTemplate;

import java.util.UUID;

public interface UserFormService {
    public FormTemplate getTemplateById(UUID id);
}
