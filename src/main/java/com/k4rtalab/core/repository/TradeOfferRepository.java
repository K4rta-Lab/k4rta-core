package com.k4rtalab.core.repository;

import com.k4rtalab.core.domain.TradeOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TradeOfferRepository extends JpaRepository<TradeOffer, UUID> {
    List<TradeOffer> findByRequestId(UUID requestId);
}