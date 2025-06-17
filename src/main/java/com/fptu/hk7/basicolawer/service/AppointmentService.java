package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerResponse;
import com.fptu.hk7.basicolawer.pojo.Appointment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {
    List<AppointmentLawyerResponse> findAllAppointmentLawyer();

    Optional<Appointment> findById(UUID id);

    void createAppointmentLawyer(AppointmentLawyerResponse appointment);

    Appointment update(UUID id, Appointment appointment);

    void deleteById(UUID id);

    List<AppointmentLawyerResponse> findAllAppointmentUser();
}
