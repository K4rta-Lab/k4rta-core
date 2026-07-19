package com.k4rtalab.core.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateOfferRequest {
    @NotNull
    private UUID requestId;
    @NotNull
    private UUID offeredCardId;
}