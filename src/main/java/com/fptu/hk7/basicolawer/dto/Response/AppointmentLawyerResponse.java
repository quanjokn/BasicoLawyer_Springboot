package com.fptu.hk7.basicolawer.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentLawyerResponse {
    @JsonProperty("bookingId")
    private UUID bookingId;
    private UUID id;
    private String customerFullName;
    private String customerPhonne;
    private String customerEmail;
    private LocalDateTime dateBooking;
    private int slot;
    private String channel;
    private String status;
    private String linkCall;
    private String note;
}
