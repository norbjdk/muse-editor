package com.muse.server.controller;

import com.muse.server.model.detail.CustomUserDetails;
import com.muse.server.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload/avatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("file")MultipartFile file
            ) {
        final String url = storageService.uploadAvatar(user.getId(), file);

        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("/projects/{projectId}/file/upload")
    public ResponseEntity<Map<String, String>> uploadProjectFile(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file
    ) {
        final String url = storageService.uploadProjectFile(user.getId(), projectId, file);

        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("/projects/{projectId}/cover/upload")
    public ResponseEntity<Map<String, String>> uploadProjectCover(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file
    ) {
        final String url = storageService.uploadProjectCover(user.getId(), projectId, file);

        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/projects/{projectId}/file/get")
    public ResponseEntity<Map<String,String >> getProjectFile(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId
    )  {
        final String url = storageService.getProjectFileUrl(user.getId(), projectId);

        return ResponseEntity.ok(Map.of("url", url));
    }
}
