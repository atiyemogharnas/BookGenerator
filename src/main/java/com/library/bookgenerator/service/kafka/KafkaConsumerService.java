package com.library.bookgenerator.service.kafka;

import com.library.bookgenerator.entity.Book;
import com.library.bookgenerator.repository.BookRepository;
import com.library.bookgenerator.utils.ConvertDate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Arrays;


@Service
public class KafkaConsumerService {

    private static final String directoryPath = "D:\\Educational Mahsan\\bookGenerator\\src\\main\\resources";
//    private static final String directoryPath = "/app/resources";
    private static final List<String> importantOwners = Arrays.asList(" Sadegh Hedayat", " Darren Hardy");

    @Autowired
    private BookRepository bookRepository;

//    @KafkaListener(topics = "high-priority-topic", groupId = "priority-group", containerFactory = "kafkaListenerContainerFactory")
//    public void consumeHighPriority(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
//        System.out.printf("Processing HIGH-priority message: %s%n", record.value());
//
//        processMessage(record);
//        acknowledgment.acknowledge();
//    }
//
//    @KafkaListener(topics = "low-priority-topic", groupId = "priority-group", containerFactory = "kafkaListenerContainerFactory")
//    public void consumeLowPriority(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
//        System.out.printf("Processing LOW-priority message: %s%n", record.value());
//
//        processMessage(record);
//        acknowledgment.acknowledge();
//    }

    private boolean processingHighPriority = false;

    @KafkaListener(topics = {"high-priority-topic", "low-priority-topic"}, groupId = "priority-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeMessages(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        if (record.topic().equals("high-priority-topic")) {
            System.out.printf("Processing HIGH-priority message: %s%n", record.value());
            processMessage(record);
            acknowledgment.acknowledge();
            processingHighPriority = true;
        } else if (record.topic().equals("low-priority-topic") && processingHighPriority) {
            System.out.printf("Processing LOW-priority message: %s%n", record.value());
            processMessage(record);
            acknowledgment.acknowledge();
        } else {
            System.out.printf("Waiting for high-priority processing to finish for message: %s%n", record.value());
        }
    }

    public void processMessage(ConsumerRecord<String, String> record) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String message = record.value();
        System.out.println("Received message: " + message + " _ " + "topic:" + record.topic());

        String[] messageParts = record.value().split("\\|");
        String title = messageParts[0];
        String owner = messageParts.length > 1 ? messageParts[1] : "Unknown";

        try {
            processBook(title, owner);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void processBook(String title, String author) {
        File file = new File(directoryPath + "/", (title.replaceFirst("\\s", "").replaceAll("\\s+", "_")) + ".txt");
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }
        String line;
        Date createdDate = null;
        StringBuilder content = new StringBuilder();
        boolean isContentSection = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Creation Date")) {
                    createdDate = ConvertDate.convertStrToDate(line.substring(14).trim());
                } else if (line.startsWith("Content")) {
                    isContentSection = true;
                } else if (isContentSection) {
                    content.append(line).append("\n");
                }
            }

            Book book = new Book(title, content.toString(), createdDate, author);
            bookRepository.save(book);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
