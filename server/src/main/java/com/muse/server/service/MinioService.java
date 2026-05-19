package com.muse.server.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucket;

    @Value("${minio.endpoint}")
    private String endpoint;

    // ── init ────────────────────────────────────────────

    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build()
                );
                applyPublicReadPolicy();
            }
        } catch (Exception e) {
            throw new RuntimeException("MinIO init failed", e);
        }
    }

    private void applyPublicReadPolicy() throws Exception {
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
                """.formatted(bucket);

        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder().bucket(bucket).config(policy).build()
        );
    }

    public void createUserFolder(Long userId) {
        createEmptyObject(userId + "/avatar/.keep");
        createEmptyObject(userId + "/projects/.keep");
    }

    private void createEmptyObject(String path) {
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
        validateImageType(file);
        String ext = getExtension(file.getContentType());
        String path = userId + "/avatar/avatar" + ext;
        putObject(path, file);
        return buildUrl(path);
    }

    public String uploadProjectFile(Long userId, Long projectId, MultipartFile file) {
        validateMusicXmlType(file);
        String ext = getExtension(file.getContentType());
        String path = userId + "/projects/" + projectId + "/score" + ext;
        putObject(path, file);
        return buildUrl(path);
    }

    public String uploadProjectCover(Long userId, Long projectId, MultipartFile file) {
        validateImageType(file);
        String ext = getExtension(file.getContentType());
        String path = userId + "/projects/" + projectId + "/cover" + ext;
        putObject(path, file);
        return buildUrl(path);
    }

    private void putObject(String path, MultipartFile file) {
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

    public String getProjectFileUrl(Long userId, Long projectId) {
        for (String ext : List.of(".musicxml", ".xml", ".mxl")) {
            String path = userId + "/projects/" + projectId + "/score" + ext;
            if (objectExists(path)) return buildUrl(path);
        }
        throw new RuntimeException("Project file not found for projectId: " + projectId);
    }

    public InputStream downloadProjectFile(Long userId, Long projectId) {
        for (String ext : List.of(".musicxml", ".xml", ".mxl")) {
            String path = userId + "/projects/" + projectId + "/score" + ext;
            if (objectExists(path)) return getObject(path);
        }
        throw new RuntimeException("Project file not found for projectId: " + projectId);
    }

    private InputStream getObject(String path) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucket).object(path).build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Download failed: " + path, e);
        }
    }

    public void deleteProjectFiles(Long userId, Long projectId) {
        for (String ext : List.of(".musicxml", ".xml", ".mxl")) {
            String path = userId + "/projects/" + projectId + "/score" + ext;
            if (objectExists(path)) deleteObject(path);
        }
        for (String ext : List.of(".jpg", ".png", ".webp")) {
            String path = userId + "/projects/" + projectId + "/cover" + ext;
            if (objectExists(path)) deleteObject(path);
        }
    }

    private void deleteObject(String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucket).object(path).build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: " + path, e);
        }
    }

    private boolean objectExists(String path) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucket).object(path).build()
            );
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Stat failed: " + path, e);
        }
    }

    private String buildUrl(String path) {
        return String.format("%s/%s/%s", endpoint, bucket, path);
    }

    private void validateImageType(MultipartFile file) {
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }
    }

    private void validateMusicXmlType(MultipartFile file) {
        List<String> allowed = List.of(
                "application/xml",
                "text/xml",
                "application/vnd.recordare.musicxml+xml",
                "application/vnd.recordare.musicxml",
                "application/octet-stream"
        );
        String ct = file.getContentType();
        String name = file.getOriginalFilename();

        if (ct != null && allowed.contains(ct)) return;

        if (name != null && (name.endsWith(".musicxml") || name.endsWith(".xml") || name.endsWith(".mxl"))) return;

        throw new IllegalArgumentException("Invalid file type: " + ct);
    }

    private String getExtension(String contentType) {
        if (contentType == null) return ".bin";
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
}