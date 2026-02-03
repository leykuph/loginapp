package com.loginapp.controller;
import com.loginapp.model.User;
import com.loginapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Minimal controller to test: insert user, fetch user by username.
 * Step 4: hash password with BCrypt on register. Step 5: login.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * POST /users
     * Body: { "username": "alice", "password": "secret" }
     * Validates, hashes password with BCrypt, then saves. Never stores plain password.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body("username and password required");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("username already exists");
        }
        String passwordHash = passwordEncoder.encode(password);
        User user = new User(username, passwordHash);
        user = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", user.getId(),
                "username", user.getUsername()
        ));
    }

    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body("username and password required");
        }
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (passwordEncoder.matches(password, user.getPasswordHash())) {
                        return ResponseEntity.ok(Map.of(
                                "id", user.getId(),
                                "username", user.getUsername()
                        ));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials"));
    }

    /**
     * GET /users/{username}
     * Fetches user by username. Never returns password_hash.
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(user -> ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
