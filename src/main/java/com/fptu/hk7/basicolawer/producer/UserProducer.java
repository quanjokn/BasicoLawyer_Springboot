package com.fptu.hk7.basicolawer.producer;

import com.fptu.hk7.basicoeventdata.event.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProducer {
    private final String CREATE = "user-create";
    private final String UPDATE = "user-update";
    private final String DELETE = "user-delete";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void produce(UserEvent userEvent, String topic) {
        try {
            System.out.println("START user event: " + topic);
            if (CREATE.equals(topic)) {
                kafkaTemplate.send(CREATE, userEvent);
            } else if(DELETE.equals(topic)) {
                kafkaTemplate.send(DELETE, userEvent);
            } else if(UPDATE.equals(topic)) {
                kafkaTemplate.send(UPDATE, userEvent);
            }
            System.out.println("END user event: " + topic);
        } catch (Exception e) {
            System.out.println("x ERROR send event with topic" + topic + ": " + e.getMessage());
        }

    }
}
