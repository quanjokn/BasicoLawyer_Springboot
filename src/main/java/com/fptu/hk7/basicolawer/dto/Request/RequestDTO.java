package com.fptu.hk7.basicolawer.dto.Request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class RequestDTO {
    private LocalDate dateBooking;
    private LocalDate dateRequest;
    private String note;
    private int slot;
    private UUID userId; // Chỉ gửi userId từ frontend
    private UUID preferedLawerUUID;
    private int amount;
    private String selectedLawyerUUID; // UUID của luật sư được chọn

    private List<String> specialties; // Danh sách chuyên môn của luật sư
}
