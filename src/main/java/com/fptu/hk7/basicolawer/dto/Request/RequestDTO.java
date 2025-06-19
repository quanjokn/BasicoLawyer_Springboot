package com.fptu.hk7.basicolawer.dto.Request;

import com.fptu.hk7.basicolawer.pojo.Services;
import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import com.fptu.hk7.basicolawer.pojo.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class RequestDTO {
    private UUID id;
    private LocalDate dateBooking;
    private LocalDate dateRequest;
    private String note;
    private int slot;
    private UUID userId; // Chỉ gửi userId từ frontend
    private UUID preferedLawerUUID;
    private String staffName;
    private int amount;
    private List<String> services;
    private UserDTO user; // Thông tin người dùng
    private StaffProfile staffProfile;// Danh sách chuyên môn của luật sư
    private String channel;
    private String status;
    private String linkCall;
}
