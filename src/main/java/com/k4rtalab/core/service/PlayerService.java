package com.k4rtalab.core.service;

import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.exception.K4rtaException;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Player findById(UUID id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + id));
    }

    @Transactional(readOnly = true)
    public Player findByUsername(String username) {
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + username));
    }

    @Transactional
    public Player register(String username, String email, String rawPassword) {
        if (playerRepository.existsByUsername(username))
            throw new K4rtaException("Username already exists: " + username);

        if (playerRepository.existsByEmail(email))
            throw new K4rtaException("Email already registered: " + email);

        Player player = Player.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .build();

        return playerRepository.save(player);
    }
}