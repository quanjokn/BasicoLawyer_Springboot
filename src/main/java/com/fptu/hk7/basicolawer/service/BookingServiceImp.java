package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Response.BookingLawyerDto;
import com.fptu.hk7.basicolawer.dto.Response.ServicesDto;
import com.fptu.hk7.basicolawer.pojo.Booking;
import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.pojo.Services;
import com.fptu.hk7.basicolawer.pojo.User;
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
            BookingLawyerDto bookingLawyerDto = new BookingLawyerDto();

            bookingLawyerDto.setId(booking.getId());

            Request request = booking.getRequest();

            User customer = request.getUser();
            bookingLawyerDto.setCustomerFullName(customer.getFullname());
            bookingLawyerDto.setCustomerPhone(customer.getPhone());
            bookingLawyerDto.setCustomerEmail(customer.getEmail());
            bookingLawyerDto.setDateBooking(request.getDateBooking());
            bookingLawyerDto.setStatus(request.getStatus());

            List<Services> listServices = request.getServices();
            List<ServicesDto> servicesDtos = new ArrayList<>();
            for (Services service : listServices) {
                ServicesDto servicesDto = new ServicesDto();
                servicesDto.setId(service.getId());
                servicesDto.setServiceName(service.getName());
                servicesDtos.add(servicesDto);
            }
            bookingLawyerDto.setServices(servicesDtos);
            bookingLawyerDto.setSlot(request.getSlot());

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
    public Booking update(UUID id, Booking booking) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        booking.setId(id);
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteById(UUID id) {
        bookingRepository.deleteById(id);
    }
}
