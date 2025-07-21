package com.fptu.hk7.basicolawer.service.Quartz;

import com.fptu.hk7.basicolawer.pojo.Appointment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentSchedulerService {
    private final Scheduler scheduler;

    public void schedulerReminder(Appointment appointment) {
        LocalDateTime startTime = getLocalDateTime(appointment);

        LocalDateTime reminderCustomerEmailTime = startTime.minusMinutes(30);
//        LocalDateTime reminderCustomerEmailTime = LocalDateTime.of(2025, 7, 19, 0,5);
        LocalDateTime reminderLawyerEmailTime = startTime.minusMinutes(5);

        try{
            log.info("Scheduling for appointment {}, email: {}, slot: {}",
                    appointment.getId(),
                    appointment.getBooking().getUser().getEmail(),
                    appointment.getSlot()
            );
            JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                    .withIdentity("reminder_appointment_"+appointment.getId(), "reminder_job_detail_group")
                    .usingJobData("appointmentId", String.valueOf(appointment.getId()))
                    .usingJobData("customerName", appointment.getBooking().getRequest().getUser().getFullname())
                    .usingJobData("customerEmail", appointment.getBooking().getRequest().getUser().getEmail())
                    .usingJobData("lawyerName", appointment.getLawyerFullName())
                    .usingJobData("dateTime", startTime.toString())
                    .usingJobData("slot", appointment.getSlot().toString())
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("reminder_trigger_appointment_" + appointment.getId(), "reminder_trigger_group")
                    .startAt(java.sql.Timestamp.valueOf(reminderCustomerEmailTime))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("Failed to schedule appointment reminders", e);

            throw new RuntimeException("Failed to schedule appointment reminders", e);
        }
    }

    private static @NotNull LocalDateTime getLocalDateTime(Appointment appointment) {
        int slot = appointment.getSlot();
        Map<Integer, LocalTime> slotTimeMap = Map.of(
                1, LocalTime.of(8, 0),
                2, LocalTime.of(10, 0),
                3, LocalTime.of(13, 0),
                4, LocalTime.of(15, 0)
        );

        LocalTime selectedTime = slotTimeMap.get(slot);
        if (selectedTime == null) throw new IllegalArgumentException("Invalid slot: " + slot);

        return LocalDateTime.of(appointment.getAssignedAt(), selectedTime);
    }
}
