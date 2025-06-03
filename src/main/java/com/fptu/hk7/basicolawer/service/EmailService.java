package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.exception.AppException;
import com.fptu.hk7.basicolawer.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendHtmlEmail(String recipient, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            System.out.println("Email sent to: " + recipient);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.INVALID_EMAIL);
        }
    }

    // Method to send OTP for password reset
    public String subject_forgotPasswordEmail = "Password Reset OTP";

    public String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    public String bodyForgotPasswordEmail(String otp) {
        return "<h1>Your OTP for password reset</h1>" +
                      "<p>Use the following OTP to reset your password:</p>" +
                      "<h2>" + otp + "</h2>" +
                      "<p>This OTP is valid for 5 minutes.</p>";
    }

}
