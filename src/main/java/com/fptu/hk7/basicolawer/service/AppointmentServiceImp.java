package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerResponse;
import com.fptu.hk7.basicolawer.enums.PaymentStatusEnum;
import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import com.fptu.hk7.basicolawer.pojo.Appointment;
import com.fptu.hk7.basicolawer.pojo.Booking;
import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.repository.AppointmentRepository;
import com.fptu.hk7.basicolawer.repository.BookingRepository;
import com.fptu.hk7.basicolawer.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentServiceImp implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserService userService;

    public AppointmentServiceImp(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

//    @Override
//    public List<Appointment> findAll() {
//        return appointmentRepository.findAll();
//    }

    @Override
    public List<AppointmentLawyerResponse> findAllAppointmentLawyer() {
        User currentLawyer = userService.getCurrentUser();
        List<Booking> listBooking = bookingRepository.findAllByUserAndRequest_Status(currentLawyer, RequestStatusEnum.ASSIGNED);
        List<AppointmentLawyerResponse> appointmentLawyerResponses = new ArrayList<>();
        for (Booking booking : listBooking) {
            Request request = booking.getRequest();
            Appointment appointment = appointmentRepository.findByBooking(booking);
            AppointmentLawyerResponse appointmentLawyerResponse = new AppointmentLawyerResponse();
            appointmentLawyerResponse.setId(appointment.getId());
            appointmentLawyerResponse.setBookingId(booking.getId());
            appointmentLawyerResponse.setDateBooking(appointment.getAssignedAt());
            appointmentLawyerResponse.setChannel(appointment.getChannel());
            appointmentLawyerResponse.setLinkCall(appointment.getLinkCall());
            appointmentLawyerResponse.setNote(appointment.getNote());
            appointmentLawyerResponse.setBookingId(booking.getId());
            appointmentLawyerResponse.setCustomerFullName(request.getUser().getFullname());
            appointmentLawyerResponse.setCustomerPhonne(request.getUser().getPhone());
            appointmentLawyerResponse.setCustomerEmail(request.getUser().getEmail());
            appointmentLawyerResponse.setStatus(appointment.getStatus());
            appointmentLawyerResponse.setSlot(appointment.getSlot());
            appointmentLawyerResponses.add(appointmentLawyerResponse);

        }
        return appointmentLawyerResponses;
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public void createAppointmentLawyer(AppointmentLawyerResponse appointmentLawyer) {
        Appointment appointment = new Appointment();
        Booking booking = bookingRepository.findById(appointmentLawyer.getBookingId()).get();
        Request request = booking.getRequest();

        appointment.setAssignedAt(appointmentLawyer.getDateBooking());
        appointment.setChannel(appointmentLawyer.getChannel());
        appointment.setLinkCall(appointmentLawyer.getLinkCall());
        appointment.setNote(appointmentLawyer.getNote());
        appointment.setSlot(request.getSlot());
        appointment.setStatus("ASSIGNED");
        appointment.setBooking(booking);

        request.setStatus(RequestStatusEnum.ASSIGNED);

        appointmentRepository.save(appointment);
    }

//    @Override
//    public void save(AppointmentLawyerResponse appointmentLawyer) {
//        Appointment appointment = new Appointment();
//
//    }

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
