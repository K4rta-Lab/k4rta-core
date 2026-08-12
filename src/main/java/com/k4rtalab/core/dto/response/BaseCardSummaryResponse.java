package com.k4rtalab.core.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BaseCardSummaryResponse {
    private UUID id;
    private String cardName;
    private String rarity;
    private String slug;
    private int statGlamour;
    private int statShade;
    private int statEnergy;
}