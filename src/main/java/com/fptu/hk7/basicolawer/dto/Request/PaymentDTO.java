package com.fptu.hk7.basicolawer.dto.Request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PaymentDTO {
    private String method; // PaymentMethodEnum
    private int amount;
    private String status; // PaymentStatusEnum
    private LocalDate dateBooking;
    private LocalDate dateRequest;
    private UUID id; // UUID of the payment
    private UUID requestId;
    private LocalDate createdAt;
}
