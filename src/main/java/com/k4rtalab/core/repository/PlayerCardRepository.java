package com.k4rtalab.core.repository;

import com.k4rtalab.core.domain.Player;
import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.dto.model.CardCollectionItemResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerCardRepository extends JpaRepository<PlayerCard, UUID> {
    List<PlayerCard> findByOwner(Player owner);

    @Query("""
        SELECT new com.k4rtalab.core.dto.model.CardCollectionItemResponse(
               bc.id, bc.name, r.name, bc.slug,
               bc.statGlamour, bc.statShade, bc.statEnergy, COUNT(pc)
        )
        FROM PlayerCard pc
        JOIN pc.baseCard bc
        JOIN bc.rarity r
        WHERE pc.owner.id = :ownerId
        GROUP BY bc.id, bc.name, r.name, bc.slug, bc.statGlamour, bc.statShade, bc.statEnergy, r.tier
        ORDER BY r.tier DESC, bc.name ASC
        """)
    List<CardCollectionItemResponse> findCollectionByOwnerId(@Param("ownerId") UUID owner);

    List<PlayerCard> findByOwnerId(UUID ownerId, Sort sort);

    boolean existsByOwnerIdAndBaseCardId(UUID ownerId, UUID baseCardId);

    Optional<PlayerCard> findFirstByOwnerIdAndBaseCardId(UUID ownerId, UUID baseCardId);
}