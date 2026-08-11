package com.k4rtalab.core.dto.response;

import java.util.UUID;

public record BaseCardSummaryResponse(
        UUID id,
        String cardName,
        String slug
) {}
