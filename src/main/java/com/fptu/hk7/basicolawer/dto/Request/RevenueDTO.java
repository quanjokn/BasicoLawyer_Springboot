package com.fptu.hk7.basicolawer.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDTO {
    private String serviceName;
    private int totalTransactions;
    private long totalAmount;
    private double changePercent;
    private List<PaymentDTO> payments;
}
