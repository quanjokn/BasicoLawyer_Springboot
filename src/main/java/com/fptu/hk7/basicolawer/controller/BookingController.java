package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Request.StatusDTO;
import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import com.fptu.hk7.basicolawer.dto.Response.BookingLawyerDto;
import com.fptu.hk7.basicolawer.pojo.Booking;
import com.fptu.hk7.basicolawer.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Booking> getById(@PathVariable("id") UUID id) {
        Optional<Booking> booking = bookingService.findById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        return bookingService.save(booking);
    }

    @PreAuthorize("hasRole('CONSULTANT')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") UUID id, @RequestBody StatusDTO status) {
        bookingService.updateStatusBooking(id, status);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(200)
                        .message("Update booking status successfully")
                        .result(null)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
