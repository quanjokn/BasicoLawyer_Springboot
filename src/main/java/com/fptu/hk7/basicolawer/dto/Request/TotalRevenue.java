package com.fptu.hk7.basicolawer.dto.Request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalRevenue {
    private long totalAmount;
    private List<PaymentDTO> payments;
}
