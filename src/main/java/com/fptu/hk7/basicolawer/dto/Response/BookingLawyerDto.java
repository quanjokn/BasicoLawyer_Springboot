package com.fptu.hk7.basicolawer.dto.Response;


import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BookingLawyerDto {
    private UUID id;
    private String customerFullName;
    private String customerPhone;
    private String customerEmail;
    private int slot;
    private List<ServicesDto> services;
    private LocalDate dateBooking;
    private RequestStatusEnum status;
}
