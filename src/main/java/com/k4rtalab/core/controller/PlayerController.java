package com.k4rtalab.core.controller;

import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.dto.request.RecycleRequest;
import com.k4rtalab.core.dto.response.PlayerCardResponse;
import com.k4rtalab.core.dto.response.PlayerStatsResponse;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.exception.UnauthorizedActionException;
import com.k4rtalab.core.repository.PlayerCardRepository;
import com.k4rtalab.core.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {
    private final CardService cardService;
    private final PlayerCardRepository playerCardRepository;

    @GetMapping("/me/stats")
    public ResponseEntity<PlayerStatsResponse> getStats(@AuthenticationPrincipal Player player) {
        return ResponseEntity.ok(toStatsResponse(player));
    }

    @GetMapping("/me/collection")
    public ResponseEntity<List<PlayerCardResponse>> getCollection(@AuthenticationPrincipal Player player) {
        List<PlayerCard> cards = cardService.findCardsByOwner(player.getId());
        return ResponseEntity.ok(cards.stream().map(PlayerController::toCardResponse).toList());
    }

    @PostMapping("/me/cards/recycle")
    public ResponseEntity<Void> recycle(@AuthenticationPrincipal Player player, @Valid @RequestBody RecycleRequest request) {
        List<PlayerCard> cards = playerCardRepository.findAllById(request.getCardIds());

        if (cards.size() != request.getCardIds().size())
            throw new ResourceNotFoundException("Some cards were not found");

        if (!cards.stream().allMatch(card -> card.getOwner().getId().equals(player.getId())))
            throw new UnauthorizedActionException("Card does not belong to this player");

        // TODO: calcular monedas según raridad de cada carta y dárselas al jugador
        playerCardRepository.deleteAllInBatch(cards);

        return ResponseEntity.noContent().build();
    }

    // --- Mappers ---

    private static PlayerStatsResponse toStatsResponse(Player player) {
        return PlayerStatsResponse.builder()
                .id(player.getId())
                .username(player.getUsername())
                .email(player.getEmail())
                .coins(player.getCoins())
                .lastReward(player.getLastReward())
                .createdAt(player.getCreatedAt())
                .build();
    }

    private static PlayerCardResponse toCardResponse(PlayerCard card) {
        return PlayerCardResponse.builder()
                .id(card.getId())
                .baseCardId(card.getBaseCard().getId())
                .cardName(card.getBaseCard().getName())
                .rarity(card.getRarity().getName())
                .imageUrl(card.getBaseCard().getImageUrl())
                .recycleValue(card.getRarity().getRecycleValue())
                .statHp(card.getStatHp())
                .statAtk(card.getStatAtk())
                .statDef(card.getStatDef())
                .statSpd(card.getStatSpd())
                .obtainedAt(card.getObtainedAt())
                .build();
    }
}
