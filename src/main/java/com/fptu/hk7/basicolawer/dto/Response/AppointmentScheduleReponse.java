package com.fptu.hk7.basicolawer.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentScheduleReponse {
    private UUID id;
    private UUID bookingId;
    private String customerFullName;
    private String customerEmail;
    private String customerPhone;
    private String lawyerFullName;
    private LocalDate dateBooking;
    private int slot;
    private String channel;
    private String status;
    private String linkCall;
    private String note;
}
