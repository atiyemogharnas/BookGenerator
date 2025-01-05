package com.library.bookgenerator.service.kafka;

import com.library.bookgenerator.service.minio.MinioService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class BookLoaderService {

    private static final String directoryPath = "D:\\Educational Mahsan\\bookGenerator\\src\\main\\resources";
//    private static final String directoryPath = "/app/resources";
    private final KafkaProducerService kafkaProducerService;
    private final MinioService minioService;

    public BookLoaderService(KafkaProducerService kafkaProducerService, MinioService minioService) {
        this.kafkaProducerService = kafkaProducerService;
        this.minioService = minioService;
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
                        InputStream stream =  new FileInputStream(file);
                        MultipartFile multipartFileToSend = new MockMultipartFile("file", file.getName(), MediaType.TEXT_HTML_VALUE, stream);
                        minioService.uploadFileToMinio(file.getName(),multipartFileToSend.getBytes());
                        processBookFile(file.toPath());
                    } catch (Exception ex) {
                       throw new RuntimeException(ex.getMessage());
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
            sendToKafka(title, author);
        }
    }

    private void sendToKafka(String title, String owner) {
        System.out.println("sending to kafka");
        kafkaProducerService.sendMessage(title.split(":")[1],  owner.split(":")[1]);
    }


    @PostConstruct
    public void init() {
        processBooks();
    }
}
