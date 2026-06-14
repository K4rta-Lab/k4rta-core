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
    private String imageUrl;
    private int statHp;
    private int statAtk;
    private int statDef;
    private int statSpd;
    private LocalDateTime obtainedAt;
}