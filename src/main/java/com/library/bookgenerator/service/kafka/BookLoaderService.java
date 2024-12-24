package com.library.bookgenerator.service.kafka;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class BookLoaderService {

    private static final String directoryPath = "D:\\Educational Mahsan\\bookGenerator\\src\\main\\resources";
    private final KafkaProducerService kafkaProducerService;

    public BookLoaderService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void processBooks() {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new RuntimeException("Directory does not exist or is not a directory");
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try {
                        processBookFile(file.toPath());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }

    public void processBookFile(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        if (!lines.isEmpty()) {
            String title = lines.get(0);
            String author = lines.get(1);

            System.out.println("Title: " + title + " Author: " + author);
            sendToKafka(title, author);
        }
    }

    private void sendToKafka(String title, String author) {
        System.out.println("sending to kafka");
        String message = String.format("%s, %s", title, author);
        kafkaProducerService.sendMessage(message);
    }


    @PostConstruct
    public void init() {
        processBooks();
    }
}
