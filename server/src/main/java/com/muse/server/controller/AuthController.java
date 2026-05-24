package com.muse.server.controller;

import com.muse.server.model.dto.auth.AuthRequest;
import com.muse.server.model.dto.auth.AuthResponse;
import com.muse.server.model.dto.user.RegisterRequest;
import com.muse.server.model.entity.UserEntity;
import com.muse.server.service.AuthService;
import com.muse.server.service.UserService;
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
        AuthResponse response = authService.auth(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserEntity user = userService.registerUser(request);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(request.getUsername());
        authRequest.setPassword(request.getPassword());

        AuthResponse response = authService.auth(authRequest);
        return ResponseEntity.ok(response);
    }
}
