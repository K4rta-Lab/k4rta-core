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
import java.util.UUID;

@Repository
public interface PlayerCardRepository extends JpaRepository<PlayerCard, UUID> {
    List<PlayerCard> findByOwner(Player owner);

    @Query("""
            SELECT new com.k4rtalab.core.dto.model.CardCollectionItemResponse(
                   pc.id, bc.name, r.name, bc.slug,
                   pc.statHp, pc.statAtk, pc.statDef, pc.statSpd
            )
            FROM PlayerCard pc
            JOIN pc.baseCard bc
            JOIN pc.rarity r
            WHERE pc.owner.id = :ownerId
            ORDER BY r.tier DESC, bc.name ASC
            """)
    List<CardCollectionItemResponse> findCollectionByOwnerId(@Param("ownerId") UUID owner);

    List<PlayerCard> findByOwnerId(UUID ownerId, Sort sort);
}