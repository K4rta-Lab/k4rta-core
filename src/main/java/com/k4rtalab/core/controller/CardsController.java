package com.k4rtalab.core.controller;

import com.k4rtalab.core.domain.BaseCard;
import com.k4rtalab.core.dto.response.BaseCardSummaryResponse;
import com.k4rtalab.core.mapper.BaseCardMapper;
import com.k4rtalab.core.repository.BaseCardRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Cards", description = "API for managing cards")
@SecurityRequirement(name = "Bearer Authentication")
public class CardsController {

    private final BaseCardRepository baseCardRepository;
    private final BaseCardMapper baseCardMapper;

    @Operation(summary = "Get all base cards or search by name")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of base cards",
             content = @Content(mediaType = "application/json")
    )
    @GetMapping("/base")
    public ResponseEntity<List<BaseCardSummaryResponse>> getBaseCards(
            @RequestParam(required = false) String search
    ) {
        List<BaseCard> cards = (search == null || search.isBlank())
                ? baseCardRepository.findAll()
                : baseCardRepository.findByNameContainingIgnoreCase(search);

        return ResponseEntity.ok(baseCardMapper.toSummaryResponses(cards));
    }
}