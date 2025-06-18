package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.StatusDTO;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentScheduleReponse;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerListResponse;
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
    public List<AppointmentLawyerListResponse> findAllAppointmentLawyer() {
        User currentLawyer = userService.getCurrentUser();
        List<Booking> listBooking = bookingRepository.findAllByUserAndRequest_Status(currentLawyer, RequestStatusEnum.ASSIGNED);
        List<AppointmentLawyerListResponse> appointmentLawyerList = new ArrayList<>();
        for (Booking booking : listBooking) {
            Request request = booking.getRequest();
            List<Appointment> appointmentList = appointmentRepository.findAllByBooking(booking);

            List<AppointmentLawyerResponse> appointmentLawyerResponses = new ArrayList<>();
            for (Appointment appointment : appointmentList) {
                AppointmentLawyerResponse appointmentLawyerResponse = AppointmentLawyerResponse.builder()
                        .id(appointment.getId())
                        .dateBooking(appointment.getAssignedAt())
                        .channel(appointment.getChannel())
                        .linkCall(appointment.getLinkCall())
                        .note(appointment.getNote())
                        .slot(appointment.getSlot())
                        .status(appointment.getStatus())
                        .bookingId(booking.getId())
                        .build();
                appointmentLawyerResponses.add(appointmentLawyerResponse);
            }

            AppointmentLawyerListResponse appointmentLawyerListResponse = AppointmentLawyerListResponse.builder()
                    .customerFullName(request.getUser().getFullname())
                    .customerPhone(request.getUser().getPhone())
                    .customerEmail(request.getUser().getEmail())
                    .appointmentLawyers(appointmentLawyerResponses)
                    .build();

            appointmentLawyerList.add(appointmentLawyerListResponse);
        }
        return appointmentLawyerList;
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public void createAppointmentLawyer(AppointmentLawyerResponse appointmentLawyer) {

        Booking booking = bookingRepository.findById(appointmentLawyer.getBookingId()).get();
        User lawyer = booking.getUser();
        Request request = booking.getRequest();
        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID())
                .assignedAt(appointmentLawyer.getDateBooking())
                .channel(appointmentLawyer.getChannel())
                .linkCall(appointmentLawyer.getLinkCall())
                .note(appointmentLawyer.getNote())
                .slot(appointmentLawyer.getSlot())
                .status("PENDING")
                .lawyerFullName(lawyer.getFullname())
                .lawerUUID(lawyer.getStaffProfile().getId())
                .paymentStatus(PaymentStatusEnum.PAID)
                .booking(booking)
                .build();

        request.setStatus(RequestStatusEnum.ASSIGNED);

        appointmentRepository.save(appointment);
    }

//    @Override
//    public void save(AppointmentLawyerResponse appointmentLawyer) {
//        Appointment appointment = new Appointment();
//
//    }

    @Override
    public AppointmentLawyerResponse updateStatusAppointment(UUID id, StatusDTO status) {
        Appointment appointment = appointmentRepository.findById(id).get();
        if (appointment != null) {
            appointment.setStatus(status.getStatus());
            appointmentRepository.save(appointment);
        }
        AppointmentLawyerResponse appointmentLawyerResponse = AppointmentLawyerResponse.builder()
                .id(appointment.getId())
                .bookingId(appointment.getBooking().getId())
                .dateBooking(appointment.getAssignedAt())
                .channel(appointment.getChannel())
                .linkCall(appointment.getLinkCall())
                .note(appointment.getNote())
                .status(appointment.getStatus())
                .slot(appointment.getSlot())
                .build();
       return appointmentLawyerResponse;
    }

    @Override
    public void deleteById(UUID id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentLawyerResponse> findAllAppointmentUser() {
    User currentUser = userService.getCurrentUser();
    List<Request> request = currentUser.getRequests();
    for(Request req : request) {
        Booking booking = req.getBooking();
    }
    List<AppointmentLawyerResponse> appointmentLawyerResponses = new ArrayList<>();

        return List.of();
    }

    @Override
    public List<AppointmentScheduleReponse> findAllAppointmentAdmin() {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentScheduleReponse> appointmentAdminResponses = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentScheduleReponse appointmentScheduleReponse = AppointmentScheduleReponse.builder()
                    .id(appointment.getId())
                    .bookingId(appointment.getBooking().getId())
                    .customerFullName(appointment.getBooking().getRequest().getUser().getFullname())
                    .customerEmail(appointment.getBooking().getRequest().getUser().getEmail())
                    .customerPhone(appointment.getBooking().getRequest().getUser().getPhone())
                    .lawyerFullName(appointment.getBooking().getUser().getFullname())
                    .dateBooking(appointment.getAssignedAt())
                    .slot(appointment.getSlot())
                    .channel(appointment.getChannel())
                    .status(appointment.getStatus())
                    .linkCall(appointment.getLinkCall())
                    .note(appointment.getNote())
                    .build();
            appointmentAdminResponses.add(appointmentScheduleReponse);
        }
        return appointmentAdminResponses;
    }

    @Override
    public List<AppointmentScheduleReponse> findScheduleLawyer() {
        User currentUser = userService.getCurrentUser();
        List<Appointment> appointments = appointmentRepository.findAllByLawyerFullName(currentUser.getFullname());
        List<AppointmentScheduleReponse> appointmentAdminResponses = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentScheduleReponse appointmentScheduleReponse = AppointmentScheduleReponse.builder()
                    .id(appointment.getId())
                    .bookingId(appointment.getBooking().getId())
                    .customerFullName(appointment.getBooking().getRequest().getUser().getFullname())
                    .customerEmail(appointment.getBooking().getRequest().getUser().getEmail())
                    .customerPhone(appointment.getBooking().getRequest().getUser().getPhone())
                    .lawyerFullName(appointment.getBooking().getUser().getFullname())
                    .dateBooking(appointment.getAssignedAt())
                    .slot(appointment.getSlot())
                    .channel(appointment.getChannel())
                    .status(appointment.getStatus())
                    .linkCall(appointment.getLinkCall())
                    .note(appointment.getNote())
                    .build();
            appointmentAdminResponses.add(appointmentScheduleReponse);
        }
        return appointmentAdminResponses;
    }
}
