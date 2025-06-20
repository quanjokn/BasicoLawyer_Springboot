package com.fptu.hk7.basicolawer.dto.Request;

import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PaymentDTO {
    private String method; // PaymentMethodEnum
    private int amount;
    private String status; // PaymentStatusEnum
    private LocalDate dateBooking;
    private LocalDate dateRequest;
    private UUID paymentId; // UUID of the payment
    private UUID requestId;// UUID of the request associated with the payment
}
