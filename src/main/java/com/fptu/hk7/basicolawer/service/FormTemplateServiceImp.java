package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.FormTemplate;
import com.fptu.hk7.basicolawer.repository.FormTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FormTemplateServiceImp implements FormTemplateService {

    private final FormTemplateRepository formTemplateRepository;

    public FormTemplateServiceImp(FormTemplateRepository formTemplateRepository) {
        this.formTemplateRepository = formTemplateRepository;
    }

    @Override
    public List<FormTemplate> findAll() {
        return formTemplateRepository.findAll();
    }

    @Override
    public Optional<FormTemplate> findById(UUID id) {
        return formTemplateRepository.findById(id);
    }

    @Override
    public FormTemplate save(FormTemplate formTemplate) {
        return formTemplateRepository.save(formTemplate);
    }

    @Override
    public FormTemplate update(UUID id, FormTemplate formTemplate) {
        if (!formTemplateRepository.existsById(id)) {
            throw new RuntimeException("FormTemplate not found");
        }
        formTemplate.setId(id);
        return formTemplateRepository.save(formTemplate);
    }

    @Override
    public void deleteById(UUID id) {
        formTemplateRepository.deleteById(id);
    }
}
