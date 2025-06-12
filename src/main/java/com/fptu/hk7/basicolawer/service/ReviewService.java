package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewService {
    List<Review> findAll();
    Optional<Review> findById(UUID id);
    Review save(Review review);
    Review update(UUID id, Review review);
    void deleteById(UUID id);
}
