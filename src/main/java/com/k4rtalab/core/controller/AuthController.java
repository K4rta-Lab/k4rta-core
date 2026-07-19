package com.k4rtalab.core.controller;

import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.dto.request.LoginRequest;
import com.k4rtalab.core.dto.request.RegisterRequest;
import com.k4rtalab.core.dto.response.AuthResponse;
import com.k4rtalab.core.security.JwtUtil;
import com.k4rtalab.core.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {
    private final PlayerService playerService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Register a new player")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Player registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists", content = @Content())
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        Player player = playerService.register(request.getUsername(), request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(player);

        return ResponseEntity.ok(new AuthResponse(token, player.getId(), player.getUsername()));
    }

    @Operation(summary = "Authenticate a player and get a JWT")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content())
    })
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
