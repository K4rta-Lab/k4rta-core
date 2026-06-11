package com.k4rtalab.core.service;

import com.k4rtalab.core.domain.BaseCard;
import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.exception.UnauthorizedActionException;
import com.k4rtalab.core.repository.BaseCardRepository;
import com.k4rtalab.core.repository.PlayerCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final BaseCardRepository baseCardRepository;
    private final PlayerCardRepository playerCardRepository;

    @Transactional(readOnly = true)
    public BaseCard findBaseCardById(UUID id) {
        return baseCardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BaseCard not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<BaseCard> findAllBaseCards() {
        return baseCardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PlayerCard findPlayerCardById(UUID id) {
        return playerCardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PlayerCard not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<PlayerCard> findCardsByOwner(UUID ownerId) {
        return playerCardRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public PlayerCard distributeEvPoints(UUID playerCardId, UUID requestingPlayerId, int evHp, int evAtk, int evDef, int evSpd) {
        PlayerCard card = findPlayerCardById(playerCardId);

        if (!card.getOwner().getId().equals(requestingPlayerId))
            throw new UnauthorizedActionException("Card does not belong to this player: " + requestingPlayerId);

        int maxEv = card.getBaseCard().getRarity().getEvPoints();
        int total = evHp + evAtk + evDef + evSpd;

        if (evHp < 0 || evAtk < 0 || evDef < 0 || evSpd < 0)
            throw new IllegalArgumentException("EV points cannot be negative");

        if (total > maxEv)
            throw new IllegalArgumentException("EV points exceed limit for this rarity. Max: " + maxEv + ", got: " + total);

        card.setEvHp(evHp);
        card.setEvAtk(evAtk);
        card.setEvDef(evDef);
        card.setEvSpd(evSpd);

        return playerCardRepository.save(card);
    }
}