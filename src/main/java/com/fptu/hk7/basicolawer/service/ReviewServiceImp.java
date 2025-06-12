package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Review;
import com.fptu.hk7.basicolawer.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;

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
    public Review save(Review review) {
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
