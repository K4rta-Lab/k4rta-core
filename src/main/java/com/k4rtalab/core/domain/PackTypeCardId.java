package com.k4rtalab.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PackTypeCardId implements Serializable {

    @Column(name = "pack_type_id")
    private UUID packTypeId;

    @Column(name = "base_card_id")
    private UUID baseCardId;
}