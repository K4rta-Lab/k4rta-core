package com.k4rtalab.core.controller;

import com.k4rtalab.core.domain.PackType;
import com.k4rtalab.core.domain.PackTypeCard;
import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.dto.request.OpenPackRequest;
import com.k4rtalab.core.dto.response.PackOpenResponse;
import com.k4rtalab.core.dto.response.PackTypeResponse;
import com.k4rtalab.core.dto.response.PlayerCardResponse;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.repository.PackTypeCardRepository;
import com.k4rtalab.core.repository.PackTypeRepository;
import com.k4rtalab.core.service.PackService;
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
public class PackController {

    private final PackService packService;
    private final PackTypeRepository packTypeRepository;
    private final PackTypeCardRepository packTypeCardRepository;

    @GetMapping
    public ResponseEntity<List<PackTypeResponse>> getAllPacks() {
        List<PackType> packs = packTypeRepository.findAll();
        return ResponseEntity.ok(packs.stream().map(PackController::toPackTypeResponse).toList());
    }

    @GetMapping("/pool/{packTypeId}")
    public ResponseEntity<List<String>> getPackPool(@PathVariable UUID packTypeId) {
        List<PackTypeCard> pool = packTypeCardRepository.findByPackTypeId(packTypeId);
        if (pool.isEmpty()) throw new ResourceNotFoundException("PackType not found: " + packTypeId);

        List<String> cardNames = pool.stream()
                .map(ptc -> ptc.getBaseCard().getName())
                .toList();

        return ResponseEntity.ok(cardNames);
    }

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
                .cards(cards.stream().map(PackController::toCardResponse).toList())
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

    private static PlayerCardResponse toCardResponse(PlayerCard card) {
        return PlayerCardResponse.builder()
                .id(card.getId())
                .baseCardId(card.getBaseCard().getId())
                .cardName(card.getBaseCard().getName())
                .rarity(card.getRarity().getName())
                .recycleValue(card.getRarity().getRecycleValue())
                .imageUrl(card.getBaseCard().getImageUrl())
                .statHp(card.getStatHp())
                .statAtk(card.getStatAtk())
                .statDef(card.getStatDef())
                .statSpd(card.getStatSpd())
                .obtainedAt(card.getObtainedAt())
                .build();
    }
}