package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.FormTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormTemplateService {
    List<FormTemplate> findAll();
    Optional<FormTemplate> findById(UUID id);
    FormTemplate save(FormTemplate formTemplate);
    FormTemplate update(UUID id, FormTemplate formTemplate);
    void deleteById(UUID id);
}
