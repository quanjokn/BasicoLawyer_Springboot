package com.fptu.hk7.basicolawer.dto.Response;

import com.fptu.hk7.basicolawer.pojo.StaffProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentLawyerListResponse {
    private String customerFullName;
    private String customerPhone;
    private String customerEmail;
    List<AppointmentLawyerResponse> appointmentLawyers;
}
