package com.fptu.hk7.basicolawer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

        private final KafkaTemplate<String, Object> kafkaTemplate;

        public void test() {
            Test event = new Test("Gửi tin nhắn mật qua bên mongo");
            try {
                System.out.println("Start to send event");
                kafkaTemplate.send("test", event);
                System.out.println("End.");
            } catch (Exception e) {
                System.out.println("Error sending event to mongo: " + e.getMessage());
            }
        }
        public void test2() {
            Test2 event = new Test2("Cao The Quan", "caothequan814@gmail.com");
            try {
                System.out.println("Start to send event");
                kafkaTemplate.send("test-2", event);
                System.out.println("End.");
            } catch (Exception e) {
                System.out.println("Error sending event to mongo: " + e.getMessage());
            }
        }
}
