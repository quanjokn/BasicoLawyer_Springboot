package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Appointment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {
    List<Appointment> findAll();
    Optional<Appointment> findById(UUID id);
    Appointment save(Appointment appointment);
    Appointment update(UUID id, Appointment appointment);
    void deleteById(UUID id);
}
