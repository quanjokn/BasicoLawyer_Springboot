package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import com.fptu.hk7.basicolawer.dto.Response.BookingLawyerDto;
import com.fptu.hk7.basicolawer.pojo.Booking;
import com.fptu.hk7.basicolawer.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllBookingLawyer() {
        List<BookingLawyerDto> bookingLawyerList = bookingService.findAllBookingLawyer();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(200)
                        .message("Get all booking lawyer successfully")
                        .result(bookingLawyerList)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getById(@PathVariable UUID id) {
        Optional<Booking> booking = bookingService.findById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        return bookingService.save(booking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> update(@PathVariable UUID id, @RequestBody Booking booking) {
        try {
            return ResponseEntity.ok(bookingService.update(id, booking));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
