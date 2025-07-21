package com.fptu.hk7.basicolawer.service.Quartz;


import com.fptu.hk7.basicolawer.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.scheduling.quartz.QuartzJobBean;

@RequiredArgsConstructor
public class ReminderJob extends QuartzJobBean {
    private final EmailService emailService;

    @Override
    protected void executeInternal(org.quartz.JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();

        String appointmentId = dataMap.getString("appointmentId");
        String email = dataMap.getString("customerEmail");
        String lawyerName = dataMap.getString("lawyerName");
        String customerName = dataMap.getString("customerName");
        String dateTime = dataMap.getString("dateTime");
        String slot = dataMap.getString("slot");
//        String serviceName = dataMap.getString("serviceName");

        System.out.println("EMAIL _ Gửi nhắc nhở đến: " + email);

        emailService.sendHtmlEmail(email, emailService.subject_reminderEmail, emailService.bodyAppointmentReminderEmail(customerName,
                lawyerName,
                dateTime,
                slot,
                appointmentId));
    }
}
