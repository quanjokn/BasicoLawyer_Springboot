package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.pojo.AuditLog;
import com.fptu.hk7.basicolawer.service.AuditLogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit_logs")
@SecurityRequirement(name = "bearerAuth")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public List<AuditLog> getAll() {
        return auditLogService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getById(@PathVariable("id") UUID id) {
        Optional<AuditLog> auditLog = auditLogService.findById(id);
        return auditLog.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public AuditLog create(@RequestBody AuditLog auditLog) {
        return auditLogService.save(auditLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLog> update(@PathVariable("id") UUID id, @RequestBody AuditLog auditLog) {
        try {
            return ResponseEntity.ok(auditLogService.update(id, auditLog));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        auditLogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
