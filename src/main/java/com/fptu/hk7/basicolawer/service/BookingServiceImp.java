package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.StatusDTO;
import com.fptu.hk7.basicolawer.dto.Response.BookingLawyerDto;
import com.fptu.hk7.basicolawer.dto.Response.ServicesDto;
import com.fptu.hk7.basicolawer.enums.RequestStatusEnum;
import com.fptu.hk7.basicolawer.pojo.*;
import com.fptu.hk7.basicolawer.repository.BookingRepository;
import com.fptu.hk7.basicolawer.repository.RequestRepository;
import com.fptu.hk7.basicolawer.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ServiceRepository serviceRepository;
    public BookingServiceImp(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

//    @Override
//    public List<Booking> findAll() {
//        return bookingRepository.findAll();
//    }

    @Override
    public List<BookingLawyerDto> findAllBookingLawyer() {
        User currentLawyer = userService.getCurrentUser();

        List<Booking> listBooking = bookingRepository.findAllByUser(currentLawyer);
        List<BookingLawyerDto> listBookingLawyerDto = new ArrayList<>();
        for (Booking booking : listBooking) {

            Request request = booking.getRequest();
            User customer = request.getUser();
            List<Services> listServices = request.getServices();
            List<ServicesDto> servicesDtos = new ArrayList<>();
            for (Services service : listServices) {
                ServicesDto servicesDto = new ServicesDto();
                servicesDto.setId(service.getId());
                servicesDto.setServiceName(service.getName());
                servicesDtos.add(servicesDto);
            }

            BookingLawyerDto bookingLawyerDto = BookingLawyerDto.builder()
                    .id(booking.getId())
                    .customerFullName(customer.getFullname())
                    .customerPhone(customer.getPhone())
                    .customerEmail(customer.getEmail())
                    .dateBooking(request.getDateBooking())
                    .status(request.getStatus())
                    .services(servicesDtos)
                    .slot(request.getSlot())
                    .build();

            listBookingLawyerDto.add(bookingLawyerDto);
        }
        return listBookingLawyerDto;
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void updateStatusBooking(UUID id, StatusDTO status) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            Request request = booking.getRequest();
            List<Appointment> appointments = booking.getAppointments();
            for (Appointment appointment : appointments) {

                if ((!appointment.getStatus().equals(status.getStatus()) && !appointment.getStatus().equals("CANCELLED"))) {
                    throw new IllegalArgumentException("Cannot update status during an appointment");
                }
            }
            request.setStatus(RequestStatusEnum.CLOSED);
            booking.setRequest(request);
            bookingRepository.save(booking);
        }
    }

    @Override
    public void deleteById(UUID id) {
        bookingRepository.deleteById(id);
    }
}
