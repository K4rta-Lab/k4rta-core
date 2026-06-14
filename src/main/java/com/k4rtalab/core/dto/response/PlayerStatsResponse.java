package com.k4rtalab.core.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStatsResponse {
    private UUID id;
    private String username;
    private String email;
    private int coins;
    private LocalDateTime lastReward;
    private LocalDateTime createdAt;
}