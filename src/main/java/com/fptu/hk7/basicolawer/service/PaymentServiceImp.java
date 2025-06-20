package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.PaymentDTO;
import com.fptu.hk7.basicolawer.pojo.Payment;
import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImp implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    private UserService userService;

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

    @Override
    public List<PaymentDTO> getPaymentsByCurrentUser() {
        User user = userService.getCurrentUser();
        List<Payment> payments = paymentRepository.findByRequest_User(user);

        return payments.stream().map(payment -> {
            Request request = payment.getRequest(); // đảm bảo request != null

            PaymentDTO dto = new PaymentDTO();
            dto.setMethod(payment.getMethod().name()); // Enum to String
            dto.setAmount(payment.getAmount());
            dto.setStatus(payment.getStatus().name()); // Enum to String
            dto.setPaymentId(payment.getId());

            if (request != null) {
                dto.setRequestId(request.getId());
                dto.setDateBooking(request.getDateBooking());
                dto.setDateRequest(request.getDateRequest());
            }

            return dto;
        }).collect(Collectors.toList());
    }

}
