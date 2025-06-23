package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicoeventdata.event.ReviewEvent;
import com.fptu.hk7.basicolawer.pojo.Review;
import com.fptu.hk7.basicolawer.producer.ReviewProducer;
import com.fptu.hk7.basicolawer.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final ReviewProducer reviewProducer;

    public ReviewServiceImp(ReviewRepository reviewRepository, ModelMapper modelMapper, ReviewProducer reviewProducer) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.reviewProducer = reviewProducer;
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
        reviewRepository.save(review);
        // sync data
        ReviewEvent event = modelMapper.map(review, ReviewEvent.class);
        reviewProducer.produce(event, "review-create");
        return review;
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
