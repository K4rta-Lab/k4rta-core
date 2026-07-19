package com.k4rtalab.core.service;

import com.k4rtalab.core.domain.*;
import com.k4rtalab.core.exception.K4rtaException;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.exception.UnauthorizedActionException;
import com.k4rtalab.core.repository.*;
import com.k4rtalab.core.specification.TradeSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing all trade-related operations, including listings, requests and offers.
 */
@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeListingRepository tradeListingRepository;
    private final TradeRequestRepository tradeRequestRepository;
    private final TradeOfferRepository tradeOfferRepository;
    private final PlayerCardRepository playerCardRepository;
    private final PlayerRepository playerRepository;
    private final BaseCardRepository baseCardRepository;

    // --- Trade Listings ---

    /**
     * Retrieves a paginated list of trade listings based on filters.
     *
     * @param page             The page number (0-indexed).
     * @param size             The number of items per page.
     * @param status           The status of the listing (e.g., OPEN, COMPLETED).
     * @param wantedBaseCardId The ID of the base card being sought.
     * @param ownerId          The ID of the player who created the listing.
     * @return A list of trade listings matching the criteria.
     */
    @Transactional(readOnly = true)
    public List<TradeListing> getAllListingTrades(
            int page,
            int size,
            String status,
            UUID wantedBaseCardId,
            UUID ownerId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<TradeListing> spec = Specification
                .where(TradeSpecifications.<TradeListing>hasStatus(status))
                .and(TradeSpecifications.hasWantedBaseCard(wantedBaseCardId))
                .and(TradeSpecifications.hasOwner(ownerId));

        return tradeListingRepository.findAll(spec, pageable).getContent();
    }

    /**
     * Creates a new trade listing.
     *
     * @param ownerId          The ID of the player creating the listing.
     * @param offeredCardId    The ID of the player card being offered.
     * @param wantedBaseCardId The ID of the base card being requested.
     * @return The created TradeListing.
     * @throws ResourceNotFoundException   if the player, offered card, or wanted base card is not found.
     * @throws UnauthorizedActionException if the offered card does not belong to the player.
     */
    @Transactional
    public TradeListing createListing(UUID ownerId, UUID offeredCardId, UUID wantedBaseCardId) {
        Player owner = playerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + ownerId));

        PlayerCard offeredCard = playerCardRepository.findById(offeredCardId)
                .orElseThrow(() -> new ResourceNotFoundException("PlayerCard not found: " + offeredCardId));

        BaseCard wantedCard = baseCardRepository.findById(wantedBaseCardId)
                .orElseThrow(() -> new ResourceNotFoundException("BaseCard not found: " + wantedBaseCardId));

        if (!offeredCard.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedActionException("Card does not belong to this player: " + ownerId);
        }

        TradeListing listing = TradeListing.builder()
                .owner(owner)
                .offeredCard(offeredCard)
                .wantedBaseCard(wantedCard)
                .build();

        return tradeListingRepository.save(listing);
    }

    /**
     * Cancels an existing trade listing.
     *
     * @param listingId          The ID of the listing to cancel.
     * @param requestingPlayerId The ID of the player requesting the cancellation.
     * @throws K4rtaException              if the listing is not found or not open.
     * @throws UnauthorizedActionException if the player is not authorized to cancel the listing.
     */
    @Transactional
    public void cancelListing(UUID listingId, UUID requestingPlayerId) {
        TradeListing listing = tradeListingRepository.findById(listingId)
                .orElseThrow(() -> new K4rtaException("Listing not found: " + listingId));

        if (!listing.getOwner().getId().equals(requestingPlayerId))
            throw new UnauthorizedActionException("Not authorized to cancel this listing");

        if (listing.getStatus() != TradeStatus.OPEN)
            throw new K4rtaException("Listing is not open");

        listing.setStatus(TradeStatus.CANCELLED);
        tradeListingRepository.save(listing);
    }

    // --- Trade Request ---

    /**
     * Retrieves a paginated list of trade requests based on filters.
     *
     * @param page             The page number (0-indexed).
     * @param size             The number of items per page.
     * @param status           The status of the request (e.g., OPEN, COMPLETED).
     * @param wantedBaseCardId The ID of the base card being sought.
     * @param ownerId          The ID of the player who created the request.
     * @return A list of trade requests matching the criteria.
     */
    @Transactional(readOnly = true)
    public List<TradeRequest> getAllRequestTrades(
            int page,
            int size,
            String status,
            UUID wantedBaseCardId,
            UUID ownerId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<TradeRequest> spec = Specification
                .where(TradeSpecifications.<TradeRequest>hasStatus(status))
                .and(TradeSpecifications.hasWantedBaseCard(wantedBaseCardId))
                .and(TradeSpecifications.hasOwner(ownerId));

        return tradeRequestRepository.findAll(spec, pageable).getContent();
    }

    /**
     * Finds a request byt its ID
     *
     * @param requestId The ID of the wanted request
     * @return The wanted request
     * @throws ResourceNotFoundException if the request is not found
     */
    @Transactional(readOnly = true)
    public TradeRequest getRequestById(UUID requestId) {
        return tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TradeRequest not found: " + requestId));
    }

    /**
     * Creates a new trade request.
     *
     * @param ownerId          The ID of the player creating the request.
     * @param wantedBaseCardId The ID of the base card being requested.
     * @return The created TradeRequest.
     * @throws ResourceNotFoundException if the player or wanted base card is not found.
     */
    @Transactional
    public TradeRequest createRequest(UUID ownerId, UUID wantedBaseCardId) {
        Player owner = playerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + ownerId));

        BaseCard wantedCard = baseCardRepository.findById(wantedBaseCardId)
                .orElseThrow(() -> new ResourceNotFoundException("BaseCard not found: " + wantedBaseCardId));

        TradeRequest request = TradeRequest.builder()
                .owner(owner)
                .wantedBaseCard(wantedCard)
                .build();

        return tradeRequestRepository.save(request);
    }

    /**
     * Cancels an existing trade request.
     *
     * @param requestId          The ID of the request to cancel.
     * @param requestingPlayerId The ID of the player requesting the cancellation.
     * @throws ResourceNotFoundException   if the trade request is not found.
     * @throws UnauthorizedActionException if the player is not authorized to cancel the request.
     * @throws K4rtaException              if the trade request is not open.
     */
    @Transactional
    public void cancelRequest(UUID requestId, UUID requestingPlayerId) {
        TradeRequest request = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TradeRequest not found: " + requestId));

        if (!request.getOwner().getId().equals(requestingPlayerId))
            throw new UnauthorizedActionException("Not authorized to cancel this request");

        if (request.getStatus() != TradeStatus.OPEN)
            throw new K4rtaException("TradeRequest is not open");

        request.setStatus(TradeStatus.CANCELLED);
        tradeRequestRepository.save(request);
    }

    // --- Trade Offers ---

    /**
     * Creates an offer for a trade request.
     *
     * @param requestId     The ID of the trade request to offer on.
     * @param ownerId       The ID of the player making the offer.
     * @param offeredCardId The ID of the player card being offered.
     * @return The created TradeOffer.
     * @throws ResourceNotFoundException   if the trade request, player, or offered card is not found.
     * @throws K4rtaException              if the trade request is not open.
     * @throws UnauthorizedActionException if the offered card does not belong to the offerer.
     */
    @Transactional
    public TradeOffer createOffer(UUID requestId, UUID ownerId, UUID offeredCardId) {
        TradeRequest request = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TradeRequest not found: " + requestId));

        if (request.getStatus() != TradeStatus.OPEN) {
            throw new K4rtaException("Trade request is not open");
        }

        Player offerer = playerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + ownerId));

        PlayerCard offeredCard = playerCardRepository.findById(offeredCardId)
                .orElseThrow(() -> new ResourceNotFoundException("PlayerCard not found: " + offeredCardId));

        if (!offeredCard.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedActionException("Card does not belong to this player: " + ownerId);
        }

        TradeOffer offer = TradeOffer.builder()
                .request(request)
                .offerer(offerer)
                .offeredCard(offeredCard)
                .build();

        return tradeOfferRepository.save(offer);
    }

    /**
     * Accepts a trade offer, which completes the trade.
     * The offered card is transferred to the request owner, the request is closed,
     * the accepted offer is marked as ACCEPTED, and all other pending offers for the request are rejected.
     *
     * @param offerId            The ID of the offer to accept.
     * @param requestingPlayerId The ID of the player accepting the offer (must be the trade request owner).
     * @throws ResourceNotFoundException   if the offer is not found.
     * @throws UnauthorizedActionException if the player is not authorized to accept the offer.
     * @throws K4rtaException              if the trade request is not open or the offer is not pending.
     */
    @Transactional
    public void acceptOffer(UUID offerId, UUID requestingPlayerId) {
        TradeOffer offer = tradeOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found: " + offerId));

        TradeRequest request = offer.getRequest();

        if (!request.getOwner().getId().equals(requestingPlayerId))
            throw new UnauthorizedActionException("Not authorized to accept this offer");

        if (request.getStatus() != TradeStatus.OPEN)
            throw new K4rtaException("Trade request is not open");

        if (offer.getStatus() != OfferStatus.PENDING)
            throw new K4rtaException("Offer is not pending");


        // Atomic swap — everything in the same transaction
        PlayerCard offeredCard = offer.getOfferedCard();
        offeredCard.setOwner(request.getOwner());
        playerCardRepository.save(offeredCard);

        // Close request and accept offer
        request.setStatus(TradeStatus.COMPLETED);
        offer.setStatus(OfferStatus.ACCEPTED);
        tradeRequestRepository.save(request);
        tradeOfferRepository.save(offer);

        // Reject all other pending offers for this request
        List<TradeOffer> otherOffers = tradeOfferRepository.findByRequestId(request.getId())
                .stream()
                .filter(o -> !o.getId().equals(offerId))
                .filter(o -> o.getStatus() == OfferStatus.PENDING)
                .toList();

        otherOffers.forEach(o -> o.setStatus(OfferStatus.REJECTED));
        tradeOfferRepository.saveAll(otherOffers);
    }

    /**
     * Rejects a specific trade offer.
     *
     * @param offerId            The ID of the offer to reject.
     * @param requestingPlayerId The ID of the player rejecting the offer (must be the trade request owner).
     * @throws ResourceNotFoundException   if the offer is not found.
     * @throws UnauthorizedActionException if the player is not authorized to reject the offer.
     * @throws K4rtaException              if the trade request is not open or the offer is not pending.
     */
    @Transactional
    public void rejectOffer(UUID offerId, UUID requestingPlayerId) {
        TradeOffer offer = tradeOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found: " + offerId));

        TradeRequest request = offer.getRequest();

        if (!request.getOwner().getId().equals(requestingPlayerId))
            throw new UnauthorizedActionException("Not authorized to reject this offer");

        if (request.getStatus() != TradeStatus.OPEN)
            throw new K4rtaException("Trade request is not open");

        if (offer.getStatus() != OfferStatus.PENDING)
            throw new K4rtaException("Offer is not pending");

        offer.setStatus(OfferStatus.REJECTED);
        tradeOfferRepository.save(offer);
    }
}
