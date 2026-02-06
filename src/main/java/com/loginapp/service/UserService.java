package com.loginapp.service;

import com.loginapp.dto.LoginRequest;
import com.loginapp.dto.RegisterRequest;
import com.loginapp.dto.UserResponse;
import com.loginapp.model.User;
import com.loginapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (request.getUsername().contains(" ")) {
            throw new IllegalArgumentException("Username cannot contain spaces");
        }

        if (request.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (request.getPassword().contains(" ")) {
            throw new IllegalArgumentException("Password cannot contain spaces");
        }

        if (!request.getPassword().matches(".*[^A-Za-z0-9 ].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }

        if (!request.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
        if (!request.getPassword().matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), passwordHash);
        user = userRepository.save(user);

        return new UserResponse(user.getId(), user.getUsername());
    }

    public Optional<UserResponse> login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
                .map(user -> new UserResponse(user.getId(), user.getUsername()));
    }

    public Optional<UserResponse> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserResponse(user.getId(), user.getUsername()));
    }
}
