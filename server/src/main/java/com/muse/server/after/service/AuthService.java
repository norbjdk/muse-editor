package com.muse.server.after.service;

import com.muse.server.after.detail.CustomUserDetails;
import com.muse.server.after.dto.auth.AuthRequest;
import com.muse.server.after.dto.auth.AuthResponse;
import com.muse.server.after.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse authenticate(AuthRequest authRequest) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        ));

        final CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername(authRequest.getUsername());

        final String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
        );
    }
}
