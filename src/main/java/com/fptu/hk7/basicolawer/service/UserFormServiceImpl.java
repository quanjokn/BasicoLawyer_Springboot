package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Response.FormTemplateRepsonse;
import com.fptu.hk7.basicolawer.pojo.FormTemplate;
import com.fptu.hk7.basicolawer.pojo.UserForm;
import com.fptu.hk7.basicolawer.repository.FormTemplateRepository;
import com.fptu.hk7.basicolawer.repository.UserFormRepository;
import com.fptu.hk7.basicolawer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class UserFormServiceImpl implements UserFormService {

    private final UserFormRepository userFormRepo;
    private final FormTemplateRepository formTemplateRepo;
    private final UserRepository userRepo;
    @Override
    public FormTemplate getTemplateById(UUID id) {
        return formTemplateRepo.findById(id).orElseThrow();
    }

    @Override
    public List<FormTemplateRepsonse> getAllTemplates() {
        List<FormTemplate> formTemplateList = formTemplateRepo.findAll();
        List<FormTemplateRepsonse> formTemplateRepsonseList = new ArrayList<>();
        for (FormTemplate formTemplate : formTemplateList) {
            FormTemplateRepsonse formTemplateRepsonse = convertToResponse(formTemplate);
            formTemplateRepsonseList.add(formTemplateRepsonse);
        }
        return formTemplateRepsonseList;
    }

    @Override
    public List<UserForm> getAllUserForms() {
        return userFormRepo.findAll();
    }

    public FormTemplateRepsonse convertToResponse(FormTemplate template) {
        FormTemplateRepsonse response = new FormTemplateRepsonse();
        response.setId(template.getId().toString());
        response.setName(template.getName());
        response.setCategory(template.getCategory());
        response.setDescription(template.getDescription());
        response.setImgUrl(template.getImgUrl());
        return response;
    }
}
