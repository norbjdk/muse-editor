package com.muse.server.controller;

import com.muse.server.model.dto.SheetRequest;
import com.muse.server.model.dto.SheetResponse;
import com.muse.server.model.dto.SheetUploadResponse;
import com.muse.server.model.entity.UserEntity;
import com.muse.server.model.repository.UserRepository;
import com.muse.server.service.SheetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sheets")
@CrossOrigin(origins = "*")
public class SheetController {

    @Autowired
    private SheetService sheetService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<SheetUploadResponse> uploadSheet(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "composer", required = false) String composer,
            @RequestParam(value = "difficulty", required = false) String difficulty,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "isPublic", defaultValue = "true") boolean isPublic,
            Authentication authentication) {

        try {
            SheetRequest request = new SheetRequest();
            request.setTitle(title);
            request.setComposer(composer);
            request.setDescription(description);
            request.setInstrument(key);

            SheetResponse response = sheetService.uploadSheet(file, request, authentication.getName());
            return ResponseEntity.ok(new SheetUploadResponse(true, "Sheet uploaded successfully", response));
        } catch (Exception e) {
            SheetUploadResponse errorResponse = new SheetUploadResponse(false, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<SheetResponse>> getAllSheets() {
        return ResponseEntity.ok(sheetService.getAllSheets());
    }

    @GetMapping("/public")
    public ResponseEntity<List<SheetResponse>> getPublicSheets() {
        return ResponseEntity.ok(sheetService.getPublicSheets());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<SheetResponse>> getSheetsByUser(@PathVariable String username) {
        return ResponseEntity.ok(sheetService.getSheetsByUser(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SheetResponse> getSheetById(@PathVariable Long id) {
        return ResponseEntity.ok(sheetService.getSheetById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SheetResponse>> searchSheets(@RequestParam String query) {
        return ResponseEntity.ok(sheetService.searchSheets(query));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SheetResponse>> getSheetsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(sheetService.getSheetsByCategory(category));
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<SheetResponse>> getSheetsByDifficulty(@PathVariable String difficulty) {
        return ResponseEntity.ok(sheetService.getSheetsByDifficulty(difficulty));
    }

    @GetMapping("/top")
    public ResponseEntity<List<SheetResponse>> getTopSheets() {
        return ResponseEntity.ok(sheetService.getTopSheets());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<SheetResponse>> getRecentSheets() {
        return ResponseEntity.ok(sheetService.getRecentSheets());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SheetResponse> updateSheet(
            @PathVariable Long id,
            @Valid @RequestBody SheetRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(sheetService.updateSheet(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSheet(@PathVariable Long id, Authentication authentication) {
        sheetService.deleteSheet(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/increment-plays")
    public ResponseEntity<Void> incrementPlays(@PathVariable Long id) {
        sheetService.incrementPlays(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeSheet(@PathVariable Long id, Authentication authentication) {
        sheetService.likeSheet(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<Void> unlikeSheet(@PathVariable Long id, Authentication authentication) {
        sheetService.unlikeSheet(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(sheetService.getStats());
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<SheetResponse>> getAllSheetsForAdmin(Authentication authentication) {
        UserEntity user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"Admin".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(sheetService.getAllSheets());
    }

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Void> updateSheetStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {
        sheetService.updateSheetStatus(id, status, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDeleteSheet(@PathVariable Long id, Authentication authentication) {
        sheetService.adminDeleteSheet(id, authentication.getName());
        return ResponseEntity.ok().build();
    }
}