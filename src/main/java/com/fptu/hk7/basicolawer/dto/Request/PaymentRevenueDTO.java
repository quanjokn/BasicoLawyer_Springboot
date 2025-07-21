package com.fptu.hk7.basicolawer.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRevenueDTO {
    private UUID id;
    private long amount;
    private String date;
    private String requestInfo;
}
