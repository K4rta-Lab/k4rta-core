package com.k4rtalab.core.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TradeRequestResponse {
    private UUID id;
    private UUID ownerId;
    private String ownerUsername;
    private UUID wantedBaseCardId;
    private String wantedBaseCardName;
    private String status;
    private LocalDateTime createdAt;
}