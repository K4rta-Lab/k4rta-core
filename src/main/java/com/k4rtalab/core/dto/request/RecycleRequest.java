package com.k4rtalab.core.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecycleRequest {
    @NotEmpty
    private List<UUID> cardIds;
}