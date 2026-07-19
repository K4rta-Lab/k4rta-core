package com.k4rtalab.core.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OfferResponse {
    private UUID id;
    private UUID requestId;
    private UUID offererId;
    private String offererUsername;
    private PlayerCardResponse offeredCard;
    private String status;
    private LocalDateTime createdAt;
}