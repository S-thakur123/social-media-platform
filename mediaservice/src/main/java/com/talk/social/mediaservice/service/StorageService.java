package com.talk.social.mediaservice.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;

@Service
public class StorageService {

    private final MinioClient minioClient;

	@Value("${minio.bucket}")
    private String bucketName;

    public StorageService(MinioClient minioClient, String bucketName) {
		super();
		this.minioClient = minioClient;
		this.bucketName = bucketName;
	}

    public String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            return fileName; // Return the unique name to be saved in Post database
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }
    
    public String getDownloadUrl(String fileName) {
    	try {
            String internalUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(fileName)
                    .expiry(15, TimeUnit.MINUTES)
                    .build()
            );

            // 🔥 The Magic Trick: Swap 'minio' for 'localhost' for the browser
            return internalUrl.replace("http://minio:9000", "http://localhost:9000");
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating URL", e);
        }
    }
}