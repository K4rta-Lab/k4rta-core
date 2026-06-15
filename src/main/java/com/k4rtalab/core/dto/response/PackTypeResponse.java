package com.k4rtalab.core.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackTypeResponse {
    private UUID id;
    private String name;
    private String description;
    private int cardCount;
    private int cost;
    private LocalDateTime availableFrom;
    private LocalDateTime availableUntil;
}