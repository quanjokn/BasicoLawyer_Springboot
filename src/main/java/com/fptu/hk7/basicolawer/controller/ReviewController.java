package com.fptu.hk7.basicolawer.controller;

import com.fptu.hk7.basicolawer.dto.Request.ReviewDTO;
import com.fptu.hk7.basicolawer.pojo.Review;
import com.fptu.hk7.basicolawer.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> getAll() {
        return reviewService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable("id") UUID id) {
        Optional<Review> review = reviewService.findById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody ReviewDTO reviewdto) {
        Review review = reviewService.createReview(reviewdto);
        return ResponseEntity.ok("Review created successfully ");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable("id") UUID id, @RequestBody Review review) {
        try {
            return ResponseEntity.ok(reviewService.update(id, review));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByBookingId(@PathVariable("bookingId") UUID bookingId) {
        List<ReviewDTO> result = reviewService.findAll().stream()
                .filter(r -> r.getBooking().getId().equals(bookingId))
                .map(r -> new ReviewDTO(
                        r.getId(),
                        r.getRating(),
                        r.getBooking().getId(),
                        r.getComment() // 👈 lấy comment
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

}
