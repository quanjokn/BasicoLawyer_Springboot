package com.fptu.hk7.basicolawer.repository;

import com.fptu.hk7.basicolawer.pojo.Payment;
import com.fptu.hk7.basicolawer.pojo.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByRequest(Request request);
}
