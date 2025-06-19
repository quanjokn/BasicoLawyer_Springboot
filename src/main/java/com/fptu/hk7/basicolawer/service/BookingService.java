package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.StatusDTO;
import com.fptu.hk7.basicolawer.dto.Response.BookingLawyerDto;
import com.fptu.hk7.basicolawer.pojo.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingService {
    List<BookingLawyerDto> findAllBookingLawyer();
    Optional<Booking> findById(UUID id);
    Booking save(Booking booking);
    void updateStatusBooking(UUID id, StatusDTO status);
    void deleteById(UUID id);
}
