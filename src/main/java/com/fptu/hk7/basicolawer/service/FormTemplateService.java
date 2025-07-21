package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Response.UserFormDto;
import com.fptu.hk7.basicolawer.pojo.FormTemplate;
import com.fptu.hk7.basicolawer.pojo.UserForm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormTemplateService {
    List<FormTemplate> findAll();
    Optional<FormTemplate> findById(UUID id);
    FormTemplate save(FormTemplate formTemplate);
    FormTemplate update(UUID id, FormTemplate formTemplate);
    void deleteById(UUID id);
    List<UserFormDto> findByUser();

    Optional<FormTemplate> findByName(String templateName);
}
