package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
