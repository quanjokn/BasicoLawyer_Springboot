package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Appointment;
import com.fptu.hk7.basicolawer.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentServiceImp implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImp(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment update(UUID id, Appointment appointment) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found");
        }
        appointment.setId(id);
        return appointmentRepository.save(appointment);
    }

    @Override
    public void deleteById(UUID id) {
        appointmentRepository.deleteById(id);
    }
}
