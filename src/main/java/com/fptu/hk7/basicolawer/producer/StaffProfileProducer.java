package com.fptu.hk7.basicolawer.producer;

import com.fptu.hk7.basicoeventdata.event.StaffProfileEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffProfileProducer {
    private final String CREATE = "staff-profile-create";
    private final String UPDATE = "staff-profile-update";
    private final String DELETE = "staff-profile-delete";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void produce(StaffProfileEvent staffProfileEvent, String topic) {
        try {
            System.out.println("START staff-profile event: " + topic);
            if (CREATE.equals(topic)) {
                kafkaTemplate.send(CREATE, staffProfileEvent);
            } else if (DELETE.equals(topic)) {
                kafkaTemplate.send(DELETE, staffProfileEvent);
            } else if (UPDATE.equals(topic)) {
                System.out.println("END staff-profile event: " + topic);
            }
        } catch(Exception e){
            System.out.println("x ERROR send event staff-profile with topic" + topic + ": " + e.getMessage());
        }
    }
}