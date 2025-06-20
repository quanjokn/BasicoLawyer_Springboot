package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Appointment;
import com.fptu.hk7.basicolawer.pojo.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findAllByBooking(Booking booking);
    List<Appointment> findAllByLawyerFullName(String lawyerFullName);
    List<Appointment> findByAssignedAt(LocalDate date);
    Optional<Appointment> findByBookingId(UUID id);
}
