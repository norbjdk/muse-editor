package com.muse.server.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucket;

    @Value("${minio.endpoint}")
    private String endpoint;

    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );

            if (!exists)  {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build()
                );

            }
        } catch (Exception e) {
            throw new RuntimeException("Minio init failed", e);
        }
    }

    public void createUserFolder(Long userId) {
        createFolder(userId + "/avatar/.keep");
        createFolder(userId + "/projects/shared/.keep");
        createFolder(userId + "/projects/published/.keep");
    }

    public void createFolder(String path) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                            .contentType("application/octet-stream")
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create folder: " + path, e);
        }
    }
}
