package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.AuditLog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditLogService {
    List<AuditLog> findAll();
    Optional<AuditLog> findById(UUID id);
    AuditLog save(AuditLog auditLog);
    AuditLog update(UUID id, AuditLog auditLog);
    void deleteById(UUID id);
}
