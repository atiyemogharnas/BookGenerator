package com.library.bookgenerator.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class KafkaProducerService {

    private static final List<String> importantOwners = Arrays.asList(" Sadegh Hedayat", " Darren Hardy");

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String title, String owner) {
        String message = title + "|" + owner;
        String topic = importantOwners.contains(owner) ? "high-priority-topic" : "low-priority-topic";

        kafkaTemplate.send(topic, message);
        System.out.printf("Sent message to [%s]: %s%n", topic, message);
    }
}
