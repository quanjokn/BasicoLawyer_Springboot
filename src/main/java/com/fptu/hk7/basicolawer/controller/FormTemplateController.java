package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.pojo.FormTemplate;
import com.fptu.hk7.basicolawer.service.FormTemplateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/form-templates")
@SecurityRequirement(name = "bearerAuth")
public class FormTemplateController {

    private final FormTemplateService formTemplateService;

    public FormTemplateController(FormTemplateService formTemplateService) {
        this.formTemplateService = formTemplateService;
    }

    @GetMapping
    public List<FormTemplate> getAll() {
        return formTemplateService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormTemplate> getById(@PathVariable("id") UUID id) {
        Optional<FormTemplate> formTemplate = formTemplateService.findById(id);
        return formTemplate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public FormTemplate create(@RequestBody FormTemplate formTemplate) {
        return formTemplateService.save(formTemplate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormTemplate> update(@PathVariable("id") UUID id, @RequestBody FormTemplate formTemplate) {
        try {
            return ResponseEntity.ok(formTemplateService.update(id, formTemplate));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        formTemplateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
