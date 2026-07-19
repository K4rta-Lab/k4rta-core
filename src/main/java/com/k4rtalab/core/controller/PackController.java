package com.k4rtalab.core.controller;

import com.k4rtalab.core.domain.PackType;
import com.k4rtalab.core.domain.PackTypeCard;
import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.dto.request.OpenPackRequest;
import com.k4rtalab.core.dto.response.PackOpenResponse;
import com.k4rtalab.core.dto.response.PackTypeResponse;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.mapper.PlayerCardMapper;
import com.k4rtalab.core.repository.PackTypeCardRepository;
import com.k4rtalab.core.repository.PackTypeRepository;
import com.k4rtalab.core.service.PackService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/packs")
@RequiredArgsConstructor
@Tag(name = "Packs", description = "Endpoints for managing card packs")
@SecurityRequirement(name = "Bearer Authentication")
public class PackController {

    private final PackService packService;
    private final PackTypeRepository packTypeRepository;
    private final PackTypeCardRepository packTypeCardRepository;

    @Operation(summary = "Get all available pack types")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PackTypeResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<PackTypeResponse>> getAllPacks() {
        List<PackType> packs = packTypeRepository.findAll();
        return ResponseEntity.ok(packs.stream().map(PackController::toPackTypeResponse).toList());
    }

    @Operation(summary = "Get the card pool for a specific pack type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(type = "array", implementation = String.class)))
            ),
            @ApiResponse(responseCode = "404", description = "PackType not found", content = @Content)
    })
    @GetMapping("/pool/{packTypeId}")
    public ResponseEntity<List<String>> getPackPool(@PathVariable UUID packTypeId) {
        List<PackTypeCard> pool = packTypeCardRepository.findByPackTypeId(packTypeId);
        if (pool.isEmpty()) throw new ResourceNotFoundException("PackType not found: " + packTypeId);

        List<String> cardNames = pool.stream()
                .map(ptc -> ptc.getBaseCard().getName())
                .toList();

        return ResponseEntity.ok(cardNames);
    }

    @Operation(summary = "Open a card pack")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Pack opened successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PackOpenResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input or insufficient funds", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "PackType not found", content = @Content)
    })
    @PostMapping("/open")
    public ResponseEntity<PackOpenResponse> openPack(
            @AuthenticationPrincipal Player player,
            @Valid @RequestBody OpenPackRequest request
    ) {
        List<PlayerCard> cards = packService.openPack(player.getId(), request.getPackTypeId());

        PackType packType = packTypeRepository.findById(request.getPackTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("PackType not found: " + request.getPackTypeId()));

        long seed = cards.isEmpty() ? 0L : cards.getFirst().getPackSeed();

        PackOpenResponse response = PackOpenResponse.builder()
                .packTypeId(packType.getId())
                .packTypeName(packType.getName())
                .seed(seed)
                .cards(cards.stream().map(PlayerCardMapper::toCardResponse).toList())
                .build();

        return ResponseEntity.ok(response);
    }

    // --- Mappers ---

    private static PackTypeResponse toPackTypeResponse(PackType pack) {
        return PackTypeResponse.builder()
                .id(pack.getId())
                .name(pack.getName())
                .description(pack.getDescription())
                .cardCount(pack.getCardCount())
                .cost(pack.getCost())
                .availableFrom(pack.getAvailableFrom())
                .availableUntil(pack.getAvailableUntil())
                .build();
    }
}
