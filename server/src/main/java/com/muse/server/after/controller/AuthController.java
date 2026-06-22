package com.muse.server.after.controller;

import com.muse.server.after.dto.auth.AuthRequest;
import com.muse.server.after.dto.auth.AuthResponse;
import com.muse.server.after.dto.user.UserCreateRequest;
import com.muse.server.after.entity.UserEntity;
import com.muse.server.after.service.AuthService;
import com.muse.server.after.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        final AuthResponse response = authService.authenticate(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserCreateRequest request) {
        final UserEntity user = userService.register(request);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(request.getUsername());
        authRequest.setPassword(request.getPassword());

        AuthResponse response = authService.authenticate(authRequest);
        return ResponseEntity.ok(response);
    }
}
