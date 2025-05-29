package com.fptu.hk7.basicolawer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class VNPayController {

    private final String vnp_TmnCode = " GJOI02SJ";
    private final String vnp_HashSecret = "40CIUJZOR4264ODJU76ZN3AHSVBAYDWM";
    private final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final String vnp_ReturnUrl = "http://localhost:3000/payment-success";

    @GetMapping("/vnpay")
    public ResponseEntity<String> createVNPayUrl(@RequestParam int amount,
                                                 @RequestParam String orderInfo) throws UnsupportedEncodingException {
        String orderId = String.valueOf(System.currentTimeMillis());
        String txnRef = orderId;

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_CurrCode = "VND";
        String vnp_Locale = "vn";
        String vnp_BankCode = "NCB";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // đơn vị VNPay là x100
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        vnp_Params.put("vnp_TxnRef", txnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        vnp_Params.put("vnp_BankCode", vnp_BankCode);

        // 1. Sắp xếp
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        // 2. Tạo chuỗi dữ liệu
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String name : fieldNames) {
            String value = vnp_Params.get(name);
            if (value != null && !value.isEmpty()) {
                hashData.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                query.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
            }
        }

        hashData.setLength(hashData.length() - 1); // xóa dấu &
        query.setLength(query.length() - 1);

        // 3. Tạo chữ ký SHA512
        String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        String paymentUrl = vnp_Url + "?" + query.toString() + "&vnp_SecureHash=" + secureHash;

        return ResponseEntity.ok(paymentUrl);
    }

    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC SHA512", e);
        }
    }
}

