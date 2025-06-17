package com.fptu.hk7.basicolawer.dto.Request;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class FormPaymentDTO {
    private boolean isPaid;
    private UUID formId;
    private String paymentMethod;
    private int amount;
    private String note;
    private String txnRef;
}
