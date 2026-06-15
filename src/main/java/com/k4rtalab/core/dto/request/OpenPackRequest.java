package com.k4rtalab.core.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenPackRequest {
    @NotNull
    private UUID packTypeId;
}