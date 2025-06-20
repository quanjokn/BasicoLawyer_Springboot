package com.fptu.hk7.basicolawer.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fptu.hk7.basicolawer.dto.Request.FormPaymentDTO;
import com.fptu.hk7.basicolawer.dto.Request.RequestDTO;
import com.fptu.hk7.basicolawer.dto.Response.ApiResponse;
import com.fptu.hk7.basicolawer.dto.Response.UserFormDto;
import com.fptu.hk7.basicolawer.pojo.User;
import com.fptu.hk7.basicolawer.pojo.UserForm;
import com.fptu.hk7.basicolawer.service.FormTemplateService;
import com.fptu.hk7.basicolawer.service.UserFormService;
import com.fptu.hk7.basicolawer.service.UserService;
import com.fptu.hk7.basicolawer.service.VNPayService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forms")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class FormController {

    private final UserFormService formService;
    private final VNPayService vNPayService;
    private final UserService userService;
    private final FormTemplateService formTemplateService;

    @GetMapping
    public ResponseEntity<?> getAllTemplates() {
        return ResponseEntity.ok(formService.getAllTemplates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplate(@PathVariable UUID id) {
        return ResponseEntity.ok(formService.getTemplateById(id));
    }
    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getTemplatesUser() {
        List<UserFormDto> userFormDtoList = formTemplateService.findByUser();
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status(true)
                        .code(200)
                        .message("Get all templates successfully")
                        .result(userFormDtoList)
                        .build()
        );
    }

    @PostMapping("/payment")
    public ResponseEntity<String> createPayment(@RequestBody FormPaymentDTO formPaymentDTO, HttpServletRequest request) {
        String paymentUrl = vNPayService.createPaymentFormUrl(formPaymentDTO, request);
        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/vnpay/return")
    public void handleVNPayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        if ("00".equals(vnpResponseCode)) {
            UserForm res = vNPayService.handleVNPayFormReturn(txnRef, vnpResponseCode);
            if (res != null) {
                response.sendRedirect("http://localhost:5173/form/" + res.getTemplate().getId() + "?completed=true&txnRef="+ txnRef);
            } else {
                response.sendRedirect("http://localhost:5173/payment-failure?reason=request_not_found_or_failed");
            }
        } else {
            response.sendRedirect("http://localhost:5173/payment-failure?reason=vnpay_error&code=" + vnpResponseCode);
        }

    }
}
