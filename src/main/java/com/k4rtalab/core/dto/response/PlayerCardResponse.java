package com.k4rtalab.core.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCardResponse {
    private UUID id;
    private UUID baseCardId;
    private String cardName;
    private String rarity;
    private int recycleValue;
    private String slug;
    private int statGlamour;
    private int statShade;
    private int statEnergy;
    private LocalDateTime obtainedAt;
}