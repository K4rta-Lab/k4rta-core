package com.k4rtalab.core.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BaseCardResponse {
    private UUID id;
    private String cardName;
    private int statGlamour;
    private int statShade;
    private int statEnergy;
    private String rarity;
    private String slug;
    private LocalDateTime createdAt;
}