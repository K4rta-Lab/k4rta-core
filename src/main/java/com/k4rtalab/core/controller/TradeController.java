package com.k4rtalab.core.controller;

import com.k4rtalab.core.domain.*;
import com.k4rtalab.core.dto.request.CreateListingRequest;
import com.k4rtalab.core.dto.request.CreateOfferRequest;
import com.k4rtalab.core.dto.request.CreateRequestRequest;
import com.k4rtalab.core.dto.response.OfferResponse;
import com.k4rtalab.core.dto.response.TradeListingResponse;
import com.k4rtalab.core.dto.response.TradeRequestResponse;
import com.k4rtalab.core.mapper.PlayerCardMapper;
import com.k4rtalab.core.service.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    // todo: add filters
    @GetMapping("/boards/listing")
    public ResponseEntity<List<TradeListingResponse>> getAllListingTrades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID wantedBaseCardId,
            @RequestParam(required = false) UUID ownerId
    ) {
        return ResponseEntity.ok(
                tradeService.getAllListingTrades(page, size, status, wantedBaseCardId, ownerId)
                        .stream()
                        .map(TradeController::toListingResponse)
                        .toList()
        );
    }

    // todo: add filters
    @GetMapping("/boards/request")
    public ResponseEntity<List<TradeRequestResponse>> getAllRequestTrades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID wantedBaseCardId,
            @RequestParam(required = false) UUID ownerId
    ) {

        return ResponseEntity.ok(
                tradeService.getAllRequestTrades(page, size, status, wantedBaseCardId, ownerId)
                        .stream()
                        .map(TradeController::toRequestResponse)
                        .toList()
        );
    }

    @PostMapping("/listing")
    public ResponseEntity<TradeListingResponse> createListing(
            @AuthenticationPrincipal Player owner,
            @Valid @RequestBody CreateListingRequest request
    ) {
        return ResponseEntity.ok(toListingResponse(tradeService.createListing(owner.getId(), request.getOfferedCardId(), request.getWantedBaseCardId())));
    }

    @DeleteMapping("/listing/{listingId}")
    public ResponseEntity<Void> cancelListing(
            @AuthenticationPrincipal Player requestingPlayer,
            @PathVariable UUID listingId
    ) {
        tradeService.cancelListing(listingId, requestingPlayer.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/request")
    public ResponseEntity<TradeRequestResponse> createRequest(
            @AuthenticationPrincipal Player owner,
            @Valid @RequestBody CreateRequestRequest request
    ) {
        return ResponseEntity.ok(toRequestResponse(tradeService.createRequest(owner.getId(), request.getWantedBaseCardId())));
    }

    @DeleteMapping("/request/{requestId}")
    public ResponseEntity<Void> cancelRequest(
            @AuthenticationPrincipal Player requestingPlayer,
            @PathVariable UUID requestId
    ) {
        tradeService.cancelRequest(requestId, requestingPlayer.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/offer")
    public ResponseEntity<OfferResponse> createOffer(
            @AuthenticationPrincipal Player owner,
            @Valid @RequestBody CreateOfferRequest request
    ) {
        return toOfferResponse(tradeService.createOffer(request.getRequestId(), owner.getId(), request.getOfferedCardId()));
    }

    @PatchMapping("/offers/{offerId}/accept")
    public ResponseEntity<TradeListingResponse> acceptOffer(
            @AuthenticationPrincipal Player player,
            @PathVariable UUID offerId
    ) {
        tradeService.acceptOffer(player.getId(), offerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/offers/{offerId}/reject")
    public ResponseEntity<Void> rejectOffer(
            @AuthenticationPrincipal Player player,
            @PathVariable UUID offerId
    ) {
        tradeService.rejectOffer(offerId, player.getId());
        return ResponseEntity.noContent().build();
    }

    // --- Mappers ---

    private static TradeListingResponse toListingResponse(TradeListing listing) {
        return TradeListingResponse.builder()
                .id(listing.getId())
                .ownerId(listing.getOwner().getId())
                .ownerUsername(listing.getOwner().getUsername())
                .offeredCard(PlayerCardMapper.toCardResponse(listing.getOfferedCard()))
                .wantedBaseCardId(listing.getWantedBaseCard().getId())
                .wantedBaseCardName(listing.getWantedBaseCard().getName())
                .status(listing.getStatus().name())
                .createdAt(listing.getCreatedAt())
                .build();
    }

    private static TradeRequestResponse toRequestResponse(TradeRequest request) {
        return TradeRequestResponse.builder()
                .id(request.getId())
                .ownerId(request.getOwner().getId())
                .ownerUsername(request.getOwner().getUsername())
                .wantedBaseCardId(request.getWantedBaseCard().getId())
                .wantedBaseCardName(request.getWantedBaseCard().getName())
                .status(request.getStatus().name())
                .createdAt(request.getCreatedAt())
                .build();
    }

    private static ResponseEntity<OfferResponse> toOfferResponse(TradeOffer offer) {
        return ResponseEntity.ok(OfferResponse.builder()
                .id(offer.getId())
                .requestId(offer.getRequest().getId())
                .offererId(offer.getOfferer().getId())
                .offererUsername(offer.getOfferer().getUsername())
                .offeredCard(PlayerCardMapper.toCardResponse(offer.getOfferedCard()))
                .status(offer.getStatus().name())
                .createdAt(offer.getCreatedAt())
                .build());
    }
}
