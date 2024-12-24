package com.library.bookgenerator.service.kafka;

import com.library.bookgenerator.entity.Book;
import com.library.bookgenerator.repository.BookRepository;
import com.library.bookgenerator.utils.ConvertDate;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;

@Service
public class KafkaConsumerService {

    private static final String directoryPath = "D:\\Educational Mahsan\\bookGenerator\\src\\main\\resources";

    @Autowired
    private BookRepository bookRepository;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("book")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(topics = "book")
    public void consumeBooks(ConsumerRecord<String, String> record) {
        String message = record.value();
        System.out.println("received message: " + message);

        String[] parts = message.split(", ");
        String title = parts[0];
        String author = parts[1];
        try {
            processBook(title, author);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void processBook(String title, String author) {
        File file = new File(directoryPath + "\\", (title.split(": ")[1].replace(", ","").replaceAll("\\s+", "_")) + ".txt");
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
                } else if(line.startsWith("Content")){
                    isContentSection = true;
                }else if(isContentSection){
                    content.append(line).append("\n");
                }
            }

            Book book = new Book(title.split(":")[1], content.toString(), createdDate, author.split(":")[1]);
            bookRepository.save(book);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
