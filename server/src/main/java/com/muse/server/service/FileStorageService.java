package com.muse.server.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @PostConstruct
    public void init() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

                final String policy = """
                    {
                      "Version": "2012-10-17",
                      "Statement": [
                        {
                          "Effect": "Allow",
                          "Principal": "*",
                          "Action": ["s3:GetObject"],
                          "Resource": ["arn:aws:s3:::%s/*"]
                        }
                      ]
                    }
                    """.formatted(bucketName);

                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policy)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się zainicjalizować MinIO", e);
        }
    }

    public String uploadMusicXML(MultipartFile file) throws Exception {
        validateType(file, List.of("application/xml", "text/xml", "application/vnd.recordare.musicxml+xml", "application/vnd.recordare.musicxml"));
        return storeFile(file, "scores");
    }

    public String uploadUserProfileImage(MultipartFile file) throws Exception {
        validateImageType(file);
        return storeFile(file, "profiles");
    }

    public String uploadUserBanner(MultipartFile file) throws Exception {
        validateImageType(file);
        return storeFile(file, "banners");
    }

    private String storeFile(MultipartFile file, String folder) throws Exception {
        String extension = getFileExtension(file.getContentType());
        String fileName = folder + "/" + UUID.randomUUID() + extension;

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return getFileUrl(fileName);
    }

    private void validateType(MultipartFile file, List<String> allowedTypes) {
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Niepoprawny typ pliku: " + contentType);
        }
    }

    private void validateImageType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Plik musi być obrazem!");
        }
    }

    private String getFileExtension(String contentType) {
        return switch (contentType) {
            case "application/xml", "text/xml" -> ".xml";
            case "application/vnd.recordare.musicxml+xml" -> ".musicxml";
            case "application/vnd.recordare.musicxml" -> ".mxl";
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> ".bin";
        };
    }

    public String getFileUrl(String objectName) {
        return String.format("%s/%s/%s", endpoint, bucketName, objectName);
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public InputStream getFileStream(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }
}
