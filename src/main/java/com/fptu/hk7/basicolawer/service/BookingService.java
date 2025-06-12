package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingService {
    List<Booking> findAll();
    Optional<Booking> findById(UUID id);
    Booking save(Booking booking);
    Booking update(UUID id, Booking booking);
    void deleteById(UUID id);
}
