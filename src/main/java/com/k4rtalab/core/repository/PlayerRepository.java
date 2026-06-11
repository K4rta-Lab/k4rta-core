package com.k4rtalab.core.repository;

import com.k4rtalab.core.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Optional<Player> findByUsername(String username);

    Optional<Player> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}