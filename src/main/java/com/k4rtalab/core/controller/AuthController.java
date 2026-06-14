package com.k4rtalab.core.controller;

import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.dto.request.LoginRequest;
import com.k4rtalab.core.dto.request.RegisterRequest;
import com.k4rtalab.core.dto.response.AuthResponse;
import com.k4rtalab.core.exception.K4rtaException;
import com.k4rtalab.core.security.JwtUtil;
import com.k4rtalab.core.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PlayerService playerService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        Player player = playerService.register(request.getUsername(), request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(player);

        return ResponseEntity.ok(new AuthResponse(token, player.getId(), player.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Player player = (Player) authentication.getPrincipal();
        String token = jwtUtil.generateToken(player);

        return ResponseEntity.ok(new AuthResponse(token, player.getId(), player.getUsername()));
    }
}
