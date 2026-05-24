package com.muse.server.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

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

    public String uploadAvatar(Long userId, MultipartFile file) {
        final String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) throw new IllegalArgumentException("File must be an image");

        final String extension = extractExtension(contentType);
        final String path      = userId + "/avatar/avatar" + extension;

        addFile(path, file);

        return buildUrl(path);
    }

    public String uploadProjectFile(Long userId, Long projectId, MultipartFile file) {
        final List<String> allowed = List.of(
                "application/xml",
                "text/xml",
                "application/octet-stream"
        );

        final String contentType = file.getContentType();
        final String fileName    = file.getOriginalFilename();

        if (contentType == null || !allowed.contains(contentType)) throw new IllegalArgumentException("File must be a xml type");
        if (fileName == null || !(fileName.endsWith(".musicxml") || fileName.endsWith(".xml"))) throw new IllegalArgumentException("File must be a musicxml file");

        final String extension = extractExtension(contentType);
        final String path      = userId + "/projects/published/" + projectId + "/score"+ extension;

        addFile(path, file);

        return buildUrl(path);
    }

    public String uploadProjectCover(Long userId, Long projectId, MultipartFile file) {
        final String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) throw new IllegalArgumentException("File must be an image");

        final String extension = extractExtension(contentType);
        final String path      = userId + "/projects/published/" + projectId + "/cover" + extension;

        addFile(path, file);

        return buildUrl(path);
    }

    public String extractProjectFileUrl(Long userId, Long projectId) {
        final List<String> extensions = List.of(".musicxml", ".xml");

        for (String extension : extensions) {
            final String path = userId + "/projects/published/" + projectId + "/score" + extension;
            if (fileExists(path)) return buildUrl(path);
        }

        throw new RuntimeException("Project file not found. ID:" + projectId);
    }

    private void addFile(String path,MultipartFile  file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + path, e);
        }
    }

    private String extractExtension(String contentType) {
        if (contentType == null) return ".bin";

        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png"  -> ".png";
            case "image/webp" -> ".webp";
            default           -> ".bin";
        };
    }

    private boolean fileExists(String path) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Stat failed: " + path, e);
        }
    }

    private String buildUrl(String path) {
        return String.format("%s/%s/%s", endpoint,bucket, path);
    }
}
