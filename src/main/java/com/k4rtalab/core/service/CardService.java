package com.k4rtalab.core.service;

import com.k4rtalab.core.domain.BaseCard;
import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.dto.model.CardCollectionItemResponse;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.exception.UnauthorizedActionException;
import com.k4rtalab.core.mapper.PlayerCardMapper;
import com.k4rtalab.core.repository.BaseCardRepository;
import com.k4rtalab.core.repository.PlayerCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final BaseCardRepository baseCardRepository;
    private final PlayerCardRepository playerCardRepository;
    private final PlayerCardMapper playerCardMapper;

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
        Sort sort = Sort.by("rarity.tier").reverse().and(Sort.by("baseCard.name"));
        return playerCardRepository.findByOwnerId(ownerId, sort);
    }

    @Transactional(readOnly = true)
    public List<CardCollectionItemResponse> getPlayerCollection(UUID ownerId) {
        return playerCardRepository.findCollectionByOwnerId(ownerId);
    }
}