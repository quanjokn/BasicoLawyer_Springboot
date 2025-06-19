package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.StatusDTO;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentScheduleReponse;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerListResponse;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerResponse;
import com.fptu.hk7.basicolawer.pojo.Appointment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {
    List<AppointmentLawyerListResponse> findAllAppointmentLawyer();

    Optional<Appointment> findById(UUID id);

    void createAppointmentLawyer(AppointmentLawyerResponse appointment);

    AppointmentLawyerResponse updateStatusAppointment(UUID id, StatusDTO status);

    void deleteById(UUID id);

    List<AppointmentLawyerResponse> findAllAppointmentUser();
    List<AppointmentScheduleReponse> findAllAppointmentAdmin();
    List<AppointmentScheduleReponse> findScheduleLawyer();
}
