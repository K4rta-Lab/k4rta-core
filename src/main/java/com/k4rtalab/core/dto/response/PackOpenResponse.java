package com.k4rtalab.core.dto.response;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackOpenResponse {
    private UUID packTypeId;
    private String packTypeName;
    private long seed;
    private List<PlayerCardResponse> cards;
}