package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Response.FormTemplateRepsonse;
import com.fptu.hk7.basicolawer.pojo.FormTemplate;

import java.util.List;
import java.util.UUID;

public interface UserFormService {
     FormTemplate getTemplateById(UUID id);
     List<FormTemplateRepsonse> getAllTemplates();
}
