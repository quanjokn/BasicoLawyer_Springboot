package com.fptu.hk7.basicolawer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fptu.hk7.basicolawer.dto.Request.UserDTO;
import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import com.fptu.hk7.basicolawer.pojo.User;
import jakarta.persistence.*;
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
