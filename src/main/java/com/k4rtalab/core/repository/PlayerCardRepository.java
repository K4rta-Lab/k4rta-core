package com.k4rtalab.core.repository;

import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.domain.PlayerCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerCardRepository extends JpaRepository<PlayerCard, UUID> {
    List<PlayerCard> findByOwner(Player owner);

    List<PlayerCard> findByOwnerId(UUID ownerId);
}