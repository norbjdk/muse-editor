package com.muse.server.service;

import com.muse.server.model.detail.CustomUserDetails;
import com.muse.server.model.dto.auth.AuthRequest;
import com.muse.server.model.dto.auth.AuthResponse;
import com.muse.server.util.JwtUtil;
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

    public AuthResponse auth(AuthRequest authRequest) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername(authRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
        );
    }
}

