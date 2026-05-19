package com.muse.server.controller;

import com.muse.server.model.detail.CustomUserDetails;
import com.muse.server.service.UserStorageService;
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
    private UserStorageService userStorageService;

    // POST /api/v1/storage/avatar
    @PostMapping("/avatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("file") MultipartFile file) {

        String url = userStorageService.uploadAvatar(user.getId(), file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    // POST /api/v1/storage/projects/{projectId}/file
    @PostMapping("/projects/{projectId}/file")
    public ResponseEntity<Map<String, String>> uploadProjectFile(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file) {

        String url = userStorageService.uploadProjectFile(user.getId(), projectId, file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    // POST /api/v1/storage/projects/{projectId}/cover
    @PostMapping("/projects/{projectId}/cover")
    public ResponseEntity<Map<String, String>> uploadProjectCover(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file) {

        String url = userStorageService.uploadProjectCover(user.getId(), projectId, file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    // GET /api/v1/storage/projects/{projectId}/file
    @GetMapping("/projects/{projectId}/file")
    public ResponseEntity<Map<String, String>> getProjectFileUrl(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId) {

        String url = userStorageService.getProjectFileUrl(user.getId(), projectId);
        return ResponseEntity.ok(Map.of("url", url));
    }

    // DELETE /api/v1/storage/projects/{projectId}/file
    @DeleteMapping("/projects/{projectId}/file")
    public ResponseEntity<Void> deleteProjectFiles(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long projectId) {

        userStorageService.deleteProjectFiles(user.getId(), projectId);
        return ResponseEntity.noContent().build();
    }
}