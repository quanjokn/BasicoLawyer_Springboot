package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.AuditLog;
import com.fptu.hk7.basicolawer.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuditLogServiceImp implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImp(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }

    @Override
    public Optional<AuditLog> findById(UUID id) {
        return auditLogRepository.findById(id);
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    @Override
    public AuditLog update(UUID id, AuditLog auditLog) {
        if (!auditLogRepository.existsById(id)) {
            throw new RuntimeException("AuditLog not found");
        }
        auditLog.setId(id);
        return auditLogRepository.save(auditLog);
    }

    @Override
    public void deleteById(UUID id) {
        auditLogRepository.deleteById(id);
    }
}
