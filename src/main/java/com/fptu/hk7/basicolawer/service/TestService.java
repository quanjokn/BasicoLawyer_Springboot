//package com.fptu.hk7.basicolawer.service;
//
//import com.fptu.hk7.basicoeventdata.event.Test;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class TestService {
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//        public void test(){
//            Test event = new Test("Gửi tin nhắn mật qua bên mongo");
//            try {
//                System.out.println("Start to send event");
//                kafkaTemplate.send("test", event);
//                System.out.println("End.");
//            } catch (Exception e) {
//                System.out.println("Error sending event to mongo: " + e.getMessage());
//            }
//        }
//}
