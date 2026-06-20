package com.muse.server.before.service;

import com.muse.server.before.model.detail.CustomUserDetails;
import com.muse.server.before.model.dto.user.RegisterRequest;
import com.muse.server.before.model.dto.user.UserResponse;
import com.muse.server.before.model.entity.UserEntity;
import com.muse.server.before.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinioService minioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username)
                .or(() -> userRepo.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(user);
    }

    public UserEntity registerUser(RegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already in use");
        }

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        final UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        final UserEntity saved = userRepo.save(user);

        minioService.createUserFolder(saved.getId());

        return saved;
    }

    public UserResponse getUserByUsername(String username) {
        UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }
}
