package com.muse.server.before.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public String uploadSharedProjectFile(Long userId, Long projectId, MultipartFile file) {
        final String path = userId + "/projects/shared/" + projectId + "/score.musicxml";
        addFile(path, file);
        return buildUrl(path);
    }

    public String extractSharedProjectFileUrl(Long userId, Long projectId) {
        final String path = userId + "/projects/shared/" + projectId + "/score.musicxml";

        if (fileExists(path)) {
            try {
                return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucket)
                                .object(path)
                                .expiry(15, TimeUnit.MINUTES)
                                .build()
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate presigned URL", e);
            }
        }
        throw new RuntimeException("Shared project file not found. ID: " + projectId);
    }

    public String extractProjectFileUrl(Long userId, Long projectId) {
        final List<String> extensions = List.of(".musicxml", ".xml");

        for (String extension : extensions) {
            final String path = userId + "/projects/published/" + projectId + "/score" + extension;
            if (fileExists(path)) {
                return generatePresignedUrl(path);
            }
        }

        throw new RuntimeException("Project file not found. ID:" + projectId);
    }

    public void createProjectFolders(Long userId, Long projectId) {
        createFolder(userId + "/projects/shared/" + projectId + "/.keep");
        createFolder(userId + "/projects/published/" + projectId + "/.keep");
    }

    public String uploadSharedFile(Long userId, Long projectId, MultipartFile file) {
        final String path = userId + "/projects/shared/" + projectId + "/score.musicxml";
        addFile(path, file);
        return buildUrl(path);
    }

    public String uploadSharedFileBytes(Long userId, Long projectId, byte[] bytes) {
        final String path = userId + "/projects/shared/" + projectId + "/score.musicxml";
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("application/xml")
                            .build()
            );
            return buildUrl(path);
        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + path, e);
        }
    }

    public void copySharedToPublished(Long userId, Long projectId) {
        final String src  = userId + "/projects/shared/"    + projectId + "/score.musicxml";
        final String dest = userId + "/projects/published/" + projectId + "/score.musicxml";
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucket)
                            .object(dest)
                            .source(CopySource.builder().bucket(bucket).object(src).build())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Copy failed: " + src + " -> " + dest, e);
        }
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

    private String generatePresignedUrl(String path) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(path)
                            .expiry(10, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate presigned URL for path: " + path, e);
        }
    }

    private String extractExtension(String contentType) {
        if (contentType == null) return ".bin";
        return switch (contentType) {
            case "image/jpeg"                  -> ".jpg";
            case "image/png"                   -> ".png";
            case "image/webp"                  -> ".webp";
            case "application/xml", "text/xml" -> ".xml";
            case "application/octet-stream"    -> ".musicxml";
            default                            -> ".bin";
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
