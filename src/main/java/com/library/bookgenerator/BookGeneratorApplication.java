package com.library.bookgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.library.bookgenerator.repository")
public class BookGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookGeneratorApplication.class, args);
    }

}
