package com.fptu.hk7.basicolawer.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFormDto {
    private UUID id;
    private boolean isPaid;
    private Timestamp createdAt;
    private int amount;
    private String txnRef;
    private String paymentMethod;
    private UUID templateUUID;
    private String templateName;

}
