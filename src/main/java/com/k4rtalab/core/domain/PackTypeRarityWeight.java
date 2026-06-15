package com.k4rtalab.core.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "pack_type_rarity_weights")
public class PackTypeRarityWeight {

    @EmbeddedId
    private PackTypeRarityWeightId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("packTypeId")
    @JoinColumn(name = "pack_type_id")
    private PackType packType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rarityId")
    @JoinColumn(name = "rarity_id")
    private Rarity rarity;

    @NotNull
    @Column(nullable = false)
    private BigDecimal weight;
}