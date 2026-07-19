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
@RequestMapping("/api/trade")
@RequiredArgsConstructor
@Tag(name = "Trade", description = "Endpoints for managing trades")
@SecurityRequirement(name = "Bearer Authentication")
public class TradeController {

    private final TradeService tradeService;

    @Operation(summary = "Get all listing trades with filters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TradeListingResponse.class)))
            )
    })
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

    @Operation(summary = "Get all request trades with filters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TradeListingResponse.class)))
            )
    })
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

    @Operation(summary = "Create a new trade listing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listing created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TradeListingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Card does not belong to player", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content())
    })
    @PostMapping("/listing")
    public ResponseEntity<TradeListingResponse> createListing(
            @AuthenticationPrincipal Player owner,
            @Valid @RequestBody CreateListingRequest request
    ) {
        return ResponseEntity.ok(toListingResponse(tradeService.createListing(owner.getId(), request.getOfferedCardId(), request.getWantedBaseCardId())));
    }

    @Operation(summary = "Cancel a trade listing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Listing cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Listing not open", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Not authorized to cancel", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Listing not found", content = @Content())
    })
    @DeleteMapping("/listing/{listingId}")
    public ResponseEntity<Void> cancelListing(
            @AuthenticationPrincipal Player requestingPlayer,
            @PathVariable UUID listingId
    ) {
        tradeService.cancelListing(listingId, requestingPlayer.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new trade request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TradeRequestResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content())
    })
    @PostMapping("/request")
    public ResponseEntity<TradeRequestResponse> createRequest(
            @AuthenticationPrincipal Player owner,
            @Valid @RequestBody CreateRequestRequest request
    ) {
        return ResponseEntity.ok(toRequestResponse(tradeService.createRequest(owner.getId(), request.getWantedBaseCardId())));
    }

    @Operation(summary = "Cancel a trade request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Request cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Request not open", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Not authorized to cancel", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Request not found", content = @Content())
    })
    @DeleteMapping("/request/{requestId}")
    public ResponseEntity<Void> cancelRequest(
            @AuthenticationPrincipal Player requestingPlayer,
            @PathVariable UUID requestId
    ) {
        tradeService.cancelRequest(requestId, requestingPlayer.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create an offer for a trade request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OfferResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or request not open", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Card does not belong to player", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content())
    })
    @PostMapping("/offer")
    public ResponseEntity<OfferResponse> createOffer(
            @AuthenticationPrincipal Player owner,
            @Valid @RequestBody CreateOfferRequest request
    ) {
        return toOfferResponse(tradeService.createOffer(request.getRequestId(), owner.getId(), request.getOfferedCardId()));
    }

    @Operation(summary = "Accept a trade offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Offer accepted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TradeListingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Request not open or offer not pending", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Not authorized to accept", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content())
    })
    @PatchMapping("/offers/{offerId}/accept")
    public ResponseEntity<TradeListingResponse> acceptOffer(
            @AuthenticationPrincipal Player player,
            @PathVariable UUID offerId
    ) {
        tradeService.acceptOffer(player.getId(), offerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reject a trade offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Offer rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Offer not pending", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Not authorized to reject", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content())
    })
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
