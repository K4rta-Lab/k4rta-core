package com.k4rtalab.core.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TradeListingResponse {
    private UUID id;
    private UUID ownerId;
    private String ownerUsername;
    private BaseCardResponse offeredCard;
    private UUID wantedCardId;
    private String wantedCardName;
    private String status;
    private LocalDateTime createdAt;
}