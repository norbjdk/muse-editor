package com.muse.server.service;

import com.muse.server.model.dto.AuthRequest;
import com.muse.server.model.dto.AuthResponse;
import com.muse.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
        final UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponse(jwt, userDetails.getUsername(), null);
    }
}

