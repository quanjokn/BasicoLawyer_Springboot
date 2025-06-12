package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.pojo.Payment;
import com.fptu.hk7.basicolawer.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImp implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImp(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment update(UUID id, Payment payment) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found");
        }
        payment.setId(id);
        return paymentRepository.save(payment);
    }

    @Override
    public void deleteById(UUID id) {
        paymentRepository.deleteById(id);
    }
}
