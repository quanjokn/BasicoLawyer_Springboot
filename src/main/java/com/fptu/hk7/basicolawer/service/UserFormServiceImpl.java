package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.FormTemplate;
import com.fptu.hk7.basicolawer.repository.FormTemplateRepository;
import com.fptu.hk7.basicolawer.repository.UserFormRepository;
import com.fptu.hk7.basicolawer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
