package com.ridebooking.auth.controller;

import com.ridebooking.auth.dto.AuthResponse;
import com.ridebooking.auth.dto.LoginRequest;
import com.ridebooking.auth.dto.RegisterRequest;
import com.ridebooking.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * APIs for user registration and login.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Handles authentication related business logic
    private final AuthService authService;

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    /**
     * Login user and return JWT token.
     */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}