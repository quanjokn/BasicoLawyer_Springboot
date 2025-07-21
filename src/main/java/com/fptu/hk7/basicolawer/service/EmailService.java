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
    public String subject_reminderEmail = "BASICO LAWYER - Reminder for Upcoming Appointment";

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


    public String bodyAppointmentReminderEmail(String customerName,
                                               String lawyerName,

                                               String dateTime,
                                               String slot,
                                               String appointmentId) {

        return "<div style='font-family: Arial, sans-serif; padding: 20px;'>" +
                "<h2 style='color: #2e7d32;'>[Basico] Nhắc nhở lịch hẹn</h2>" +
                "<p>Xin chào <strong>" + customerName + "</strong>,</p>" +
                "<p>Đây là thư nhắc nhở về lịch hẹn của bạn với luật sư.</p>" +

                "<table style='width: 100%; border-collapse: collapse; margin-top: 15px;'>" +
                "  <tr><td style='padding: 6px; font-weight: bold;'>Dịch vụ:</td><td style='padding: 6px;'>Tư vấn pháp luật và biện hộ</td></tr>" +
                "  <tr style='background-color: #f9f9f9;'><td style='padding: 6px; font-weight: bold;'>Luật sư:</td><td style='padding: 6px;'>" + lawyerName + "</td></tr>" +
                "  <tr><td style='padding: 6px; font-weight: bold;'>Thời gian:</td><td style='padding: 6px;'>" + dateTime + " (" + slot + ")</td></tr>" +
                "  <tr style='background-color: #f9f9f9;'><td style='padding: 6px; font-weight: bold;'>Mã lịch hẹn:</td><td style='padding: 6px;'>" + appointmentId + "</td></tr>" +
                "</table>" +

                "<p style='margin-top: 20px;'>Vui lòng chuẩn bị và tham gia đúng giờ.</p>" +
                "<p>Trân trọng,<br><strong>Đội ngũ Basico</strong></p>" +
                "<hr style='margin-top: 30px;'>" +
                "<p style='font-size: 12px; color: #888;'>© 2025 Basico. Mọi quyền được bảo lưu.</p>" +
                "</div>";
    }

}
