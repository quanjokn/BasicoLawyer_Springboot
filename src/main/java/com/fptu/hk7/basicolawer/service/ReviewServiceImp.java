package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.ReviewDTO;
import com.fptu.hk7.basicolawer.pojo.Review;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    public ReviewServiceImp(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> findById(UUID id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Review createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        User user = userService.getCurrentUser();
        review.setCreatedAt(LocalDateTime.now());
        review.setRating(reviewDTO.getRating());
        review.setUser(user);
        review.setBooking(bookingService.findById(reviewDTO.getBookingId()).get());

        return reviewRepository.save(review);
    }

    @Override
    public Review update(UUID id, Review review) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found");
        }
        review.setId(id);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteById(UUID id) {
        reviewRepository.deleteById(id);
    }
}
