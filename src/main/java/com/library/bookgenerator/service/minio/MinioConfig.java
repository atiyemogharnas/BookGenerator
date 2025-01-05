package com.library.bookgenerator.service.minio;

import com.library.bookgenerator.utils.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        MinioClient client =  MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();

        try {
            // Check if bucket exists
            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build());
            if (!exists) {
                // Create bucket if it doesn't exist
                client.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucket()).build());
                System.out.println("Bucket " + minioProperties.getBucket() + " created successfully.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while initializing Minio bucket: " + e.getMessage(), e);
        }
        return client;
    }
}
