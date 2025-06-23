package com.fptu.hk7.basicolawer.producer;

import com.fptu.hk7.basicoeventdata.event.ReviewEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewProducer {
    private final String CREATE = "review-create";

    private final KafkaTemplate<String, Object> kafkaProducer;

    public void produce(ReviewEvent reviewEvent, String topic) {
        try {
            if(CREATE.equals(topic)) {
                kafkaProducer.send(CREATE, reviewEvent);
            }
        } catch (Exception e) {
            System.out.println("Error ReviewProducer: " + e.getMessage());
        }
    }
}
