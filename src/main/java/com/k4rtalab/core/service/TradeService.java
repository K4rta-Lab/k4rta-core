package com.k4rtalab.core.service;

import com.k4rtalab.core.domain.*;
import com.k4rtalab.core.exception.K4rtaException;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.exception.UnauthorizedActionException;
import com.k4rtalab.core.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    @Transactional
    public void cancelRequest(UUID requestId, UUID requestingPlayerId) {
        TradeRequest request = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TradeRequest not found: " + requestId));

        if (!request.getOwner().getId().equals(requestingPlayerId))
            throw new UnauthorizedActionException("Not authorized to cancel this request");

        if ( request.getStatus() != TradeStatus.OPEN)
            throw new K4rtaException("TradeRequest is not open");

        request.setStatus(TradeStatus.CANCELLED);
        tradeRequestRepository.save(request);
    }

    // --- Trade Offers ---
    @Transactional
    public TradeOffer createOffer(UUID requestId, UUID offererId, UUID offeredCardId) {
        TradeRequest request = tradeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("TradeRequest not found: " + requestId));

        if (request.getStatus() != TradeStatus.OPEN) {
            throw new K4rtaException("Trade request is not open");
        }

        Player offerer = playerRepository.findById(offererId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + offererId));

        PlayerCard offeredCard = playerCardRepository.findById(offeredCardId)
                .orElseThrow(() -> new ResourceNotFoundException("PlayerCard not found: " + offeredCardId));

        if (!offeredCard.getOwner().getId().equals(offererId)) {
            throw new UnauthorizedActionException("Card does not belong to this player: " + offererId);
        }

        TradeOffer offer = TradeOffer.builder()
                .request(request)
                .offerer(offerer)
                .offeredCard(offeredCard)
                .build();

        return tradeOfferRepository.save(offer);
    }

    @Transactional
    public void acceptOffer(UUID offerId, UUID requestingPlayerId) {
        TradeOffer offer = tradeOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found: " + offerId));

        TradeRequest request = offer.getRequest();

        if (!request.getOwner().getId().equals(requestingPlayerId)) {
            throw new UnauthorizedActionException("Not authorized to accept this offer");
        }
        if (request.getStatus() != TradeStatus.OPEN) {
            throw new K4rtaException("Trade request is not open");
        }
        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new K4rtaException("Offer is not pending");
        }

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
}