package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicoeventdata.event.FormTemplateEvent;
import com.fptu.hk7.basicolawer.dto.Response.UserFormDto;
import com.fptu.hk7.basicolawer.pojo.FormTemplate;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.pojo.UserForm;
import com.fptu.hk7.basicolawer.producer.FormTemplateProducer;
import com.fptu.hk7.basicolawer.repository.FormTemplateRepository;
import com.fptu.hk7.basicolawer.repository.UserFormRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FormTemplateServiceImp implements FormTemplateService {

    private final FormTemplateRepository formTemplateRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFormRepository userFormRepository;
    @Autowired
    private FormTemplateProducer formTemplateProducer;
    @Autowired
    private ModelMapper modelMapper;

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
        FormTemplate form =  formTemplateRepository.save(formTemplate);
        //sync
        FormTemplateEvent event = modelMapper.map(form, FormTemplateEvent.class);
        formTemplateProducer.produce(event, "form-template-create");
        return form;
    }

    @Override
    public FormTemplate update(UUID id, FormTemplate formTemplate) {
        if (!formTemplateRepository.existsById(id)) {
            throw new RuntimeException("FormTemplate not found");
        }
        formTemplate.setId(id);

        FormTemplateEvent form = modelMapper.map(formTemplate, FormTemplateEvent.class);
        formTemplateProducer.produce(form, "form-template-update");
        return formTemplateRepository.save(formTemplate);
    }

    @Override
    public void deleteById(UUID id) {
        formTemplateRepository.deleteById(id);

        FormTemplateEvent event = new FormTemplateEvent();
        event.setId(id);
        formTemplateProducer.produce(event, "form-template-delete");
    }

    @Override
    public List<UserFormDto> findByUser() {
        User user = userService.getCurrentUser();
        List<UserForm> userFormInfo = userFormRepository.findByUser(user);
        List<UserFormDto> userFormDtoList = new ArrayList<>();
        for (UserForm userForm : userFormInfo) {
            if (userForm.isPaid()) {
                UserFormDto userFormDto = UserFormDto.builder()
                        .id(userForm.getId())
                        .templateUUID(userForm.getTemplate().getId())
                        .templateName(userForm.getTemplate().getName())
                        .createdAt(userForm.getCreatedAt())
                        .isPaid(userForm.isPaid())
                        .amount(userForm.getAmount())
                        .paymentMethod(userForm.getPaymentMethod())
                        .txnRef(userForm.getTxnRef())
                        .build();
                userFormDtoList.add(userFormDto);
            }
        }
        return userFormDtoList;
    }

    @Override
    public Optional<FormTemplate> findByName(String templateName) {
        return formTemplateRepository.findByName(templateName);
    }
}
