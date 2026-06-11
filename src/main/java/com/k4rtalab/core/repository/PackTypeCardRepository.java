package com.k4rtalab.core.repository;

import com.k4rtalab.core.domain.PackTypeCard;
import com.k4rtalab.core.domain.PackTypeCardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PackTypeCardRepository extends JpaRepository<PackTypeCard, PackTypeCardId> {

    List<PackTypeCard> findByPackTypeId(UUID packTypeId);
}