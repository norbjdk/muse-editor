package com.muse.server.service;

import com.muse.server.model.dto.SheetRequest;
import com.muse.server.model.dto.SheetResponse;
import com.muse.server.model.entity.SheetEntity;
import com.muse.server.model.entity.UserEntity;
import com.muse.server.model.repository.SheetRepository;
import com.muse.server.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SheetService {

    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public SheetResponse uploadSheet(MultipartFile file, SheetRequest request, String username) throws Exception {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fileUrl = fileStorageService.uploadMusicXML(file);

        SheetEntity sheet = new SheetEntity();
        sheet.setTitle(request.getTitle());
        sheet.setArtist(request.getComposer() != null ? request.getComposer() : username);
        sheet.setDescription(request.getDescription());
        sheet.setInstrument(request.getInstrument());
        sheet.setFileUrl(fileUrl);
        sheet.setFilePath(fileUrl);
        sheet.setFileSize(file.getSize());
        sheet.setPlays(0);
        sheet.setLikes(0);
        sheet.setUser(user);

        SheetEntity savedSheet = sheetRepository.save(sheet);
        return convertToResponse(savedSheet);
    }

    public List<SheetResponse> getAllSheets() {
        return sheetRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SheetResponse> getPublicSheets() {
        return sheetRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SheetResponse> getSheetsByUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return sheetRepository.findByUserId(user.getId()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public SheetResponse getSheetById(Long id) {
        SheetEntity sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));
        return convertToResponse(sheet);
    }

    public List<SheetResponse> searchSheets(String query) {
        return sheetRepository.findAll().stream()
                .filter(sheet -> sheet.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        sheet.getArtist().toLowerCase().contains(query.toLowerCase()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SheetResponse> getSheetsByCategory(String category) {
        return sheetRepository.findAll().stream()
                .filter(sheet -> category.equalsIgnoreCase(sheet.getInstrument()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SheetResponse> getSheetsByDifficulty(String difficulty) {
        return sheetRepository.findAll().stream()
                .filter(sheet -> difficulty.equalsIgnoreCase(sheet.getInstrument()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SheetResponse> getTopSheets() {
        return sheetRepository.findTopSheets().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SheetResponse> getRecentSheets() {
        return sheetRepository.findNewestSheets().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SheetResponse updateSheet(Long id, SheetRequest request, String username) {
        SheetEntity sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));

        if (!sheet.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You don't have permission to update this sheet");
        }

        if (request.getTitle() != null) sheet.setTitle(request.getTitle());
        if (request.getComposer() != null) sheet.setArtist(request.getComposer());
        if (request.getDescription() != null) sheet.setDescription(request.getDescription());
        if (request.getInstrument() != null) sheet.setInstrument(request.getInstrument());

        return convertToResponse(sheetRepository.save(sheet));
    }

    @Transactional
    public void deleteSheet(Long id, String username) {
        SheetEntity sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));

        if (!sheet.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You don't have permission to delete this sheet");
        }

        try {
            String filePath = sheet.getFilePath();
            if (filePath != null && filePath.contains("/")) {
                String objectName = filePath.substring(filePath.lastIndexOf("/") + 1);
                fileStorageService.deleteFile(objectName);
            }
        } catch (Exception e) {
            System.err.println("Failed to delete file from storage: " + e.getMessage());
        }

        sheetRepository.delete(sheet);
    }

    @Transactional
    public void incrementPlays(Long id) {
        SheetEntity sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));
        sheet.setPlays(sheet.getPlays() + 1);
        sheetRepository.save(sheet);
    }

    @Transactional
    public void likeSheet(Long id, String username) {
        SheetEntity sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));
        sheet.setLikes(sheet.getLikes() + 1);
        sheetRepository.save(sheet);
    }

    @Transactional
    public void unlikeSheet(Long id, String username) {
        SheetEntity sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));
        sheet.setLikes(Math.max(0, sheet.getLikes() - 1));
        sheetRepository.save(sheet);
    }

    @Transactional
    public void updateSheetStatus(Long id, String status, String adminUsername) {
        UserEntity admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!"Admin".equals(admin.getRole())) {
            throw new RuntimeException("Only admins can update sheet status");
        }

        SheetEntity sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));

        sheet.setPublic("published".equals(status));
        sheetRepository.save(sheet);
    }

    @Transactional
    public void adminDeleteSheet(Long id, String adminUsername) {
        UserEntity admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!"Admin".equals(admin.getRole())) {
            throw new RuntimeException("Only admins can delete sheets");
        }

        SheetEntity sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found"));

        try {
            String filePath = sheet.getFilePath();
            if (filePath != null && filePath.contains("/")) {
                String objectName = filePath.substring(filePath.lastIndexOf("/") + 1);
                fileStorageService.deleteFile(objectName);
            }
        } catch (Exception e) {
            System.err.println("Failed to delete file: " + e.getMessage());
        }

        sheetRepository.delete(sheet);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSheets", sheetRepository.count());
        stats.put("totalPlays", sheetRepository.findAll().stream().mapToInt(SheetEntity::getPlays).sum());
        stats.put("totalLikes", sheetRepository.findAll().stream().mapToInt(SheetEntity::getLikes).sum());
        return stats;
    }

    private SheetResponse convertToResponse(SheetEntity sheet) {
        SheetResponse response = new SheetResponse();
        response.setId(sheet.getId());
        response.setTitle(sheet.getTitle());
        response.setArtist(sheet.getArtist());
        response.setFileUrl(sheet.getFileUrl());
        response.setFilePath(sheet.getFilePath());
        response.setInstrument(sheet.getInstrument());
        response.setPlays(sheet.getPlays());
        response.setLikes(sheet.getLikes());
        response.setDescription(sheet.getDescription());
        response.setCreatedAt(sheet.getCreatedAt());
        response.setUpdatedAt(sheet.getUpdatedAt());
        response.setUserId(sheet.getUser().getId());
        response.setUsername(sheet.getUser().getUsername());
        return response;
    }
}