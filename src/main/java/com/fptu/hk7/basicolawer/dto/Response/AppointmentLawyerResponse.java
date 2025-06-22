package com.fptu.hk7.basicolawer.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentLawyerResponse {
    @JsonProperty("bookingId")
    private UUID bookingId;
    private UUID id;
    private LocalDate dateBooking;
    private int slot;
    private String channel;
    private String status;
    private String linkCall;
    private Integer amount;
    private String note;
    private StaffProfile lawyerProfile;
    private List<String> services;
    private String staffName;
    private String rating;

}
