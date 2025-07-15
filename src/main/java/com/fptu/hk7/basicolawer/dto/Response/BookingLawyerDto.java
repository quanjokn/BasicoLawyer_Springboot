package com.fptu.hk7.basicolawer.dto.Response;


import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingLawyerDto {
    private UUID id;
    private String customerFullName;
    private String customerPhone;
    private String customerEmail;
    private int slot;
    private List<ServicesDto> services;
    private boolean isCancel;
    private LocalDate dateBooking;
    private RequestStatusEnum status;
}
