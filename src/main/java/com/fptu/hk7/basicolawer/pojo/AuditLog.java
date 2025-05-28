package com.fptu.hk7.basicolawer.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String actionType;

    private String targetId;

    private String targetType; // enum

    private String note;

    private LocalDateTime created;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
