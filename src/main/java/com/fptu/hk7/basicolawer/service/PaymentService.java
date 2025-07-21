package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.PaymentDTO;
import com.fptu.hk7.basicolawer.dto.Request.ReportRequest;
import com.fptu.hk7.basicolawer.dto.Request.RevenueDTO;
import com.fptu.hk7.basicolawer.dto.Response.WeekReportResponse;
import com.fptu.hk7.basicolawer.pojo.Payment;
import com.fptu.hk7.basicolawer.pojo.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentService {
    List<PaymentDTO> findAll();
    Optional<Payment> findById(UUID id);
    Payment save(Payment payment);
    Payment update(UUID id, Payment payment);
    void deleteById(UUID id);

//    List<PaymentDTO> getPaymentsByCurrentUser();

    Payment findByRequest(Request req);

    WeekReportResponse getReport(ReportRequest reportRequest);
    List<RevenueDTO> getAllServiceRevenues();
}
