package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.PaymentDTO;
import com.fptu.hk7.basicolawer.dto.Request.PaymentRevenueDTO;
import com.fptu.hk7.basicolawer.dto.Request.ReportRequest;
import com.fptu.hk7.basicolawer.dto.Request.RevenueDTO;
import com.fptu.hk7.basicolawer.dto.Response.DayReportResponse;
import com.fptu.hk7.basicolawer.dto.Response.WeekReportResponse;
import com.fptu.hk7.basicolawer.enums.PaymentStatusEnum;
import com.fptu.hk7.basicolawer.pojo.Payment;
import com.fptu.hk7.basicolawer.pojo.Request;
import com.fptu.hk7.basicolawer.pojo.Services;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.repository.PaymentRepository;
import com.fptu.hk7.basicolawer.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImp implements PaymentService {

    private final PaymentRepository paymentRepository;


    @Autowired
    private UserService userService;

    @Autowired
    private ServicesService serviceService;

    @Autowired
    private RequestService requestService;
    public PaymentServiceImp(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<PaymentDTO> findAll() {
        List<Payment> payments = paymentRepository.findAll();
        long totalAmount = payments.stream().mapToLong(Payment::getAmount).sum();

        return payments.stream()
                .map(payment -> {
                    Request request = payment.getRequest();

                    return PaymentDTO.builder()
                            .method(payment.getMethod().name())
                            .amount(payment.getAmount())
                            .status(payment.getStatus().name())
                            .id(payment.getId())
                            .createdAt(payment.getCreatedAt())
                            .totalAmount(totalAmount)
                            .requestId(request != null ? request.getId() : null)
                            .dateBooking(request != null ? request.getDateBooking() : null)
                            .dateRequest(request != null ? request.getDateRequest() : null)
                            .build();
                })
                .collect(Collectors.toList());
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

//    @Override
//    public List<PaymentDTO> getPaymentByCurrentser() {
//        List<Payment> payments = paymentRepository.findAll();
//        long totalAmount = payments.stream().mapToLong(Payment::getAmount).sum();
//
//        return payments.stream()
//                .map(payment -> {
//                    Request request = payment.getRequest();
//
//                    return PaymentDTO.builder()
//                            .method(payment.getMethod().name())
//                            .amount(payment.getAmount())
//                            .status(payment.getStatus().name())
//                            .id(payment.getId())
//                            .createdAt(payment.getCreatedAt())
//                            .totalAmount(totalAmount)
//                            .requestId(request != null ? request.getId() : null)
//                            .dateBooking(request != null ? request.getDateBooking() : null)
//                            .dateRequest(request != null ? request.getDateRequest() : null)
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }

    @Override
    public Payment findByRequest(Request req) {
        return paymentRepository.findByRequest(req).orElseThrow(()-> new RuntimeException("Payment not found"));
    }

    @Override
    public WeekReportResponse getReport(ReportRequest reportRequest) {
        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(payment -> payment.getCreatedAt().isAfter(reportRequest.getStartDate()) && payment.getCreatedAt().isBefore(reportRequest.getEndDate()) && payment.getStatus().equals(PaymentStatusEnum.PAID))
                .toList();

        // response
        WeekReportResponse weekReportResponse = new WeekReportResponse();
            // total revenue of week
        weekReportResponse.setTotalWeekRevenue(payments.stream().mapToInt(Payment::getAmount).sum());
        List<DayReportResponse> dayReports = new ArrayList<>();
            // detail
        for( LocalDate date = reportRequest.getStartDate(); date.isBefore(reportRequest.getEndDate().plusDays(1)); date = date.plusDays(1)) {
            DayReportResponse dayReport = new DayReportResponse();
            dayReport.setDate(date);
            LocalDate localDate = date;
            List<Payment> dailyPayments = payments.stream()
                    .filter(payment -> payment.getCreatedAt().equals(localDate))
                    .toList();
            dayReport.setDailyRevenue(dailyPayments.stream().mapToInt(Payment::getAmount).sum());
            dayReport.setNewRequest(dailyPayments.size());
            dayReports.add(dayReport);
        }
        weekReportResponse.setDailyReports(dayReports);

        return weekReportResponse;
    }

    @Override
    public List<RevenueDTO> getAllServiceRevenues() {
        List<Services> services = serviceService.findAll();
        List<RevenueDTO> result = new ArrayList<>();

        for (Services service : services) {
            List<Request> requests = requestService.findByServices(service);

            List<Payment> allPayments = new ArrayList<>();
            for (Request req : requests) {
                if (req.getPayment() != null) {
                    allPayments.add(req.getPayment()); // ✅ dùng add chứ không addAll
                }
            }

            long totalAmount = allPayments.stream().mapToLong(Payment::getAmount).sum();
            int totalTransactions = allPayments.size();

            List<PaymentRevenueDTO> paymentDTOs = allPayments.stream()
                    .map(p -> new PaymentRevenueDTO(
                            p.getId(),
                            p.getAmount(),
                            p.getCreatedAt().toString(), // hoặc format
                            "Request#" + p.getRequest().getId()
                    ))
                    .collect(Collectors.toList());

            result.add(new RevenueDTO(
                    service.getName(),
                    totalTransactions,
                    totalAmount, // changePercent nếu có logic
                    paymentDTOs
            ));
        }

        return result;
    }

}
