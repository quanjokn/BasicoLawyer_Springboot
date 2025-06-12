package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}
