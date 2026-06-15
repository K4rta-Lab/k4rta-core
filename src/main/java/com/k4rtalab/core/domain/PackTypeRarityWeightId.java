package com.k4rtalab.core.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class PackTypeRarityWeightId implements Serializable {
    private UUID packTypeId;
    private UUID rarityId;
}