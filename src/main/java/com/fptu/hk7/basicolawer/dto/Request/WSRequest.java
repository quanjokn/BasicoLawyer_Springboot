package com.fptu.hk7.basicolawer.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSRequest {
    private UUID id;

    private UUID preferedLawerUUID;

    private LocalDate dateBooking;

    private LocalDate dateRequest;

    private String note;

    private int slot;

    private UserDTO userInfo;
}
