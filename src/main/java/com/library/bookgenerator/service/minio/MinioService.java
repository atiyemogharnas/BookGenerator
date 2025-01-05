package com.library.bookgenerator.service.minio;

import com.library.bookgenerator.utils.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


@Service
public class MinioService {

    private static final String directoryPath = "D:\\Educational Mahsan\\bookGenerator\\src\\main\\resources";
//    private static final String directoryPath = "/app/resources";

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioProperties minioProperties;

    public void uploadFileToMinio(String fileName, byte[] content) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .stream(inputStream, content.length, -1)
                    .contentType("text/plain")
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getFile(String fileName) {
        try {

            InputStream obj = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .build());

            File file = new File(directoryPath + fileName);
            try(FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = obj.read(buffer)) != -1){
                    fos.write(buffer, 0, bytesRead);
                }
            }
//            String fileContent = new String(obj.readAllBytes(), StandardCharsets.UTF_8);
            obj.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
