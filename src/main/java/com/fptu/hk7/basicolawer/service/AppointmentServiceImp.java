package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.StatusDTO;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentScheduleReponse;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerListResponse;
import com.fptu.hk7.basicolawer.dto.Response.AppointmentLawyerResponse;
import com.fptu.hk7.basicolawer.enums.PaymentStatusEnum;
import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import com.fptu.hk7.basicolawer.pojo.*;
import com.fptu.hk7.basicolawer.repository.AppointmentRepository;
import com.fptu.hk7.basicolawer.repository.BookingRepository;
import com.fptu.hk7.basicolawer.repository.NotificationRepository;
import com.fptu.hk7.basicolawer.repository.RequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImp implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StaffProfileService staffProfileService;

    ModelMapper modelMapper = new ModelMapper();

    public AppointmentServiceImp(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

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

        Notification notification = Notification.builder()
                .message("Cuộc hẹn của bạn đã được tạo thành công vào ngày: "+appointment.getAssignedAt() + " với luật sư: " + lawyer.getFullname())
                .type("Cập nhật trạng thái dịch vụ")
                .isRead(false)
                .created(LocalDateTime.now())
                .user(request.getUser())
                .build();

        notificationService.save(notification);
        this.save(appointment);
    }


    @Override
    public AppointmentLawyerResponse updateStatusAppointment(UUID id, StatusDTO status) {
        System.out.println("Update status appointment with id: " + id + " to status: " + status.getStatus());

        Appointment appointment = appointmentRepository.findById(id).get();
        System.out.println(appointment);
        if (appointment != null) {
            if (status.getStatus().equals("CANCELLED")) {
                appointment.setSlot(0);
            }
            appointment.setStatus(status.getStatus());
            this.save(appointment);
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

        System.out.println(appointmentLawyerResponse);
        Notification notification = Notification.builder()
                .message("Cuộc hẹn của bạn đã được thay đổi trạng thái: "+status.getStatus())
                .type("Cập nhật trạng thái dịch vụ")
                .isRead(false)
                .created(LocalDateTime.now())
                .user(appointment.getBooking().getRequest().getUser())
                .build();

        System.out.println(notification);
        notificationRepository.save(notification);
        return appointmentLawyerResponse;
    }

    @Override
    public void deleteById(UUID id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentLawyerListResponse> findAllAppointmentUser() {
    User currentUser = userService.getCurrentUser();
    List<Request> requests = requestRepository.findByUserId(currentUser.getId());
    List<AppointmentLawyerResponse> result = new ArrayList<>();
        for (Request request : requests) {
            Booking booking = request.getBooking();
            if (booking == null) continue;

            // Tìm Appointment theo booking
            List<Appointment> appointments = appointmentRepository.findAllByBooking(booking);
            if (appointments.isEmpty()) continue;

            for (Appointment appointment : appointments) {
                AppointmentLawyerResponse dto = new AppointmentLawyerResponse();
                dto.setBookingId(booking.getId());
                dto.setId(appointment.getId());
                dto.setDateBooking(appointment.getAssignedAt()); // từ Request
                dto.setSlot(request.getSlot());               // từ Request
                dto.setChannel(appointment.getChannel());
                dto.setStatus(appointment.getStatus().toString());
                dto.setLinkCall(appointment.getLinkCall());
                dto.setNote(appointment.getNote());

                // Lấy thông tin số tiền từ Payment nếu có
                if (request.getPayment() != null) {
                    dto.setAmount(request.getPayment().getAmount());
                }

                // Lấy thông tin luật sư
                dto.setLawyerProfile(
                        staffProfileService.findById(appointment.getLawerUUID()).orElse(null)
                );
                dto.setStaffName(appointment.getLawyerFullName());

                // Gán dịch vụ
                if (request.getServices() != null) {
                    List<String> serviceNames = request.getServices().stream()
                            .map(Services::getName)
                            .collect(Collectors.toList());
                    dto.setServices(serviceNames);
                }

                result.add(dto);
            }
        }

        // Gom tất cả kết quả vào 1 list duy nhất, chứa thông tin khách hàng
        AppointmentLawyerListResponse appointmentLawyerListResponse = AppointmentLawyerListResponse.builder()
                .customerFullName(currentUser.getFullname())
                .customerEmail(currentUser.getEmail())
                .customerPhone(currentUser.getPhone())
                .appointmentLawyers(result)
                .build();

        return List.of(appointmentLawyerListResponse);
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

    @Override
    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);

        //sync data
//        BookingEvent event = modelMapper.map(appointment.getBooking(), BookingEvent.class);
//        BookingEvent.Appointment mgAppointment = modelMapper.map(appointment, BookingEvent.Appointment.class);
//        event.setAppointments(List.of(mgAppointment));
//        System.out.println("Booking-appointment event: "+ event);
//        bookingProducer.produce(event, "booking-appointment-create-update");
    }
}
