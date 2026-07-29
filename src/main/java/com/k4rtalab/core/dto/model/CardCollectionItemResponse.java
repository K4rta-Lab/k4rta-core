package com.k4rtalab.core.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardCollectionItemResponse {
    private UUID id;
    private String cardName;
    private String rarity;
    private String slug;
    private int statHp;
    private int statAtk;
    private int statDef;
    private int statSpd;
}
