package com.fptu.hk7.basicolawer.controller;


import com.fptu.hk7.basicolawer.service.UserFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {

    private final UserFormService formService;
    @GetMapping("/templates/{id}")
    public ResponseEntity<?> getTemplate(@PathVariable UUID id) {
        return ResponseEntity.ok(formService.getTemplateById(id));
    }
}
