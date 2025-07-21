package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.*;
import com.fptu.hk7.basicolawer.dto.Response.DayReportResponse;
import com.fptu.hk7.basicolawer.dto.Response.WeekReportResponse;
import com.fptu.hk7.basicolawer.enums.PaymentStatusEnum;
import com.fptu.hk7.basicolawer.pojo.*;
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

    private final UserFormService userFormService;


    public PaymentServiceImp(PaymentRepository paymentRepository, UserFormService userFormService) {
        this.paymentRepository = paymentRepository;
        this.userFormService = userFormService;
    }

    @Override
    public TotalRevenue findAll() {
        List<Payment> payments = paymentRepository.findAll();
        long revenue1 = payments.stream().filter(payment -> payment.getStatus().toString().equals("PAID")).mapToLong(Payment::getAmount).sum();

        List<UserForm> userForms = userFormService.getAllUserForms();
        long revenue2 = userForms.stream().filter(userForm -> userForm.isPaid() == true).mapToLong(UserForm::getAmount).sum();

        long totalAmount = revenue1 + revenue2;

        List<PaymentDTO> list = new ArrayList<>(payments.stream()
                .map(payment -> {
                    Request request = payment.getRequest();

                    return PaymentDTO.builder()
                            .method(payment.getMethod().name())
                            .amount(payment.getAmount())
                            .status(payment.getStatus().name())
                            .id(payment.getId())
                            .createdAt(payment.getCreatedAt())
                            .requestId(request != null ? request.getId() : null)
                            .dateBooking(request != null ? request.getDateBooking() : null)
                            .dateRequest(request != null ? request.getDateRequest() : null)
                            .build();
                })
                .toList());
        userForms.forEach(userForm -> {
            list.add(PaymentDTO.builder()
                    .method(userForm.getPaymentMethod())
                    .amount(userForm.getAmount())
                    .status(userForm.isPaid() ? "PAID" : "UNPAID")
                    .createdAt(userForm.getCreatedAt().toLocalDateTime().toLocalDate())
                    .requestId(UUID.randomUUID())
                    .build());
        });
        return TotalRevenue.builder()
                .payments(list)
                .totalAmount(totalAmount)
                .build();
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
        return paymentRepository.findByRequest(req).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public WeekReportResponse getReport(ReportRequest reportRequest) {
        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(payment -> payment.getCreatedAt().isAfter(reportRequest.getStartDate().minusDays(1)) && payment.getCreatedAt().isBefore(reportRequest.getEndDate().plusDays(1)) && payment.getStatus().equals(PaymentStatusEnum.PAID))
                .toList();
        List<UserForm> userForms = userFormService.getAllUserForms().stream().filter(userForm -> userForm.isPaid() == true).toList();

        // response
        WeekReportResponse weekReportResponse = new WeekReportResponse();
        // total revenue of week
        weekReportResponse.setTotalWeekRevenue(payments.stream().mapToInt(Payment::getAmount).sum() + userForms.stream().mapToInt(UserForm::getAmount).sum());
        List<DayReportResponse> dayReports = new ArrayList<>();
        // detail
        for (LocalDate date = reportRequest.getStartDate(); date.isBefore(reportRequest.getEndDate().plusDays(1)); date = date.plusDays(1)) {
            DayReportResponse dayReport = new DayReportResponse();
            dayReport.setDate(date);
            LocalDate localDate = date;
            List<Payment> dailyPayments = payments.stream()
                    .filter(payment -> payment.getCreatedAt().equals(localDate))
                    .toList();
            List<UserForm> forms = userForms.stream().filter(userForm -> userForm.getCreatedAt().toLocalDateTime().toLocalDate().equals(localDate)).toList();

            dayReport.setDailyRevenue(dailyPayments.stream().mapToInt(Payment::getAmount).sum() + forms.stream().mapToInt(UserForm::getAmount).sum());
            dayReport.setNewRequest(dailyPayments.size());
            dayReports.add(dayReport);
        }
        weekReportResponse.setDailyReports(dayReports);

        return weekReportResponse;
    }

}
