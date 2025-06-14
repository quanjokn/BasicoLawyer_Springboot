package com.fptu.hk7.basicolawer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fptu.hk7.basicolawer.dto.Request.RequestDTO;
import com.fptu.hk7.basicolawer.repository.RequestRepository;
import com.fptu.hk7.basicolawer.service.VNPayService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/payment")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/vnpay")
    public ResponseEntity<String> createVNPayUrl(@RequestBody RequestDTO dto , HttpServletRequest request) {
        String paymentUrl = vnPayService.createPaymentUrl(dto,request);
        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/vnpay/return")
    public void handleVNPayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        if ("00".equals(vnpResponseCode)) {
            RequestDTO res = vnPayService.handleVNPayReturn(txnRef, vnpResponseCode);
            System.out.println(res);
            if (res != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                String json = objectMapper.writeValueAsString(res);
                String encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString());

                response.sendRedirect("http://localhost:5173/payment-success?res=" + encoded);
            } else {
                response.sendRedirect("http://localhost:5173/payment-failure?reason=request_not_found_or_failed");
            }
            response.sendRedirect("http://localhost:5173/payment-success?txnRef=" + txnRef);
        } else {
            // Giao dịch thất bại
            response.sendRedirect("http://localhost:5173/payment-failure");
        }

    }

    @GetMapping("/api/payment/request/{txnRef}")
    public ResponseEntity<RequestDTO> getRequestByTxnRef(@PathVariable String txnRef) {
        RequestDTO dto = vnPayService.getRequestDTOByTxnRef(txnRef);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

}

