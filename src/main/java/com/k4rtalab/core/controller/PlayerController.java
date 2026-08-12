package com.k4rtalab.core.controller;

import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.dto.model.CardCollectionItemResponse;
import com.k4rtalab.core.dto.request.RecycleRequest;
import com.k4rtalab.core.dto.response.PlayerCardResponse;
import com.k4rtalab.core.dto.response.PlayerStatsResponse;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.exception.UnauthorizedActionException;
import com.k4rtalab.core.repository.PlayerCardRepository;
import com.k4rtalab.core.service.CardService;
import com.k4rtalab.core.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Tag(name = "Player", description = "Endpoints for managing player data")
@SecurityRequirement(name = "Bearer Authentication")
public class PlayerController {
    private final CardService cardService;
    private final PlayerCardRepository playerCardRepository;
    private final PlayerService playerService;

    @Operation(summary = "Get current player's stats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content()),
    })
    @GetMapping("/me/stats")
    public ResponseEntity<PlayerStatsResponse> getStats(@AuthenticationPrincipal Player player) {
        return ResponseEntity.ok(toStatsResponse(player));
    }

    @Operation(summary = "Get current player's card collection")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PlayerCardResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    })
    @GetMapping("/me/collection")
    public ResponseEntity<List<CardCollectionItemResponse>> getCollection(@AuthenticationPrincipal Player player) {
        return ResponseEntity.ok(cardService.getPlayerCollection(player.getId()));
    }

    @Operation(summary = "Recycle player cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cards recycled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
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

    @Operation(summary = "Claim daily reward")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reward claimed successfully", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Reward already claimed today", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
    })
    @GetMapping("/me/claim-reward")
    public ResponseEntity<Void> claimReward(@AuthenticationPrincipal Player player) {
        playerService.claimDailyReward(player);
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
                .rarity(card.getBaseCard().getRarity().getName())
                .recycleValue(card.getBaseCard().getRarity().getRecycleValue())
                .slug(card.getBaseCard().getSlug())
                .statGlamour(card.getBaseCard().getStatGlamour())
                .statShade(card.getBaseCard().getStatShade())
                .statEnergy(card.getBaseCard().getStatEnergy())
                .obtainedAt(card.getObtainedAt())
                .build();
    }
}
