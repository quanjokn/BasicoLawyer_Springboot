package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Appointment;
import com.fptu.hk7.basicolawer.pojo.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Appointment findByBooking(Booking booking);
}
