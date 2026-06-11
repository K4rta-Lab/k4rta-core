package com.k4rtalab.core.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pack_type_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackTypeCard {

    @EmbeddedId
    private PackTypeCardId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("packTypeId")
    @JoinColumn(name = "pack_type_id")
    private PackType packType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("baseCardId")
    @JoinColumn(name = "base_card_id")
    private BaseCard baseCard;
}