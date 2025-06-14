package com.fptu.hk7.basicolawer.dto.Response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentLawyerResponse {
    private String customerFullName;
    private String customerPhonne;
    private String customerEmail;
    private LocalDate dateBooking;
    private Integer slot;
    private String channel;
    private String linkCall;
    private String status;
    private String paymentStatus;

}
