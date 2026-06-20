package com.muse.server.after.controller;

import com.muse.server.after.dto.user.UserCreateRequest;
import com.muse.server.after.dto.user.UserResponse;
import com.muse.server.after.dto.user.UserUpdateRequest;
import com.muse.server.after.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        final UserResponse userResponse = userService.getUserByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        final UserResponse userResponse = userService.getUserByUsername(username);

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        final UserResponse updatedUser = userService.updateUser(id, request);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUser(username);

        return ResponseEntity.noContent().build();
    }
}
