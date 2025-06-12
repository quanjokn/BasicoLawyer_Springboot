package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Booking;
import com.fptu.hk7.basicolawer.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImp(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
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
