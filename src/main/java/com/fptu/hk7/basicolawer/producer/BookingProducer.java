package com.fptu.hk7.basicolawer.producer;

import com.fptu.hk7.basicoeventdata.event.BookingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingProducer {
    private final String CREATE = "booking-create";
    private final String UPDATE = "booking-update";
    private final String DELETE = "booking-delete";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void produce(BookingEvent event, String topic) {
        try {
            System.out.println("Start event " + topic + ": " + event.toString());
            if (CREATE.equals(topic)) {
                kafkaTemplate.send(CREATE, event);
            } else if (UPDATE.equals(topic)) {
                kafkaTemplate.send(UPDATE, event);
            } else if (DELETE.equals(topic)) {
                kafkaTemplate.send(DELETE, event);
            }
            System.out.println("End event " + topic);
        } catch (Exception e) {
            System.out.println("Error at RequestProducer: " + e.getMessage());
        }
    }
}
