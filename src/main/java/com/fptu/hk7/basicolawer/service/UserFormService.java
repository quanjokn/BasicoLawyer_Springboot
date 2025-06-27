package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Response.FormTemplateRepsonse;
import com.fptu.hk7.basicolawer.pojo.FormTemplate;
import com.fptu.hk7.basicolawer.pojo.UserForm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserFormService {
     UserForm save (UserForm userForm);
     FormTemplate getTemplateById(UUID id);
     List<FormTemplateRepsonse> getAllTemplates();
     Optional<UserForm> findByTxnRef(String txnRef);
}
