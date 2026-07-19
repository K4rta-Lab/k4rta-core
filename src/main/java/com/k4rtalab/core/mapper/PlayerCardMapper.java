package com.k4rtalab.core.mapper;

import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.dto.response.PlayerCardResponse;

public class PlayerCardMapper {
    private PlayerCardMapper() {}

    public static PlayerCardResponse toCardResponse(PlayerCard card) {
        return PlayerCardResponse.builder()
                .id(card.getId())
                .baseCardId(card.getBaseCard().getId())
                .cardName(card.getBaseCard().getName())
                .rarity(card.getRarity().getName())
                .recycleValue(card.getRarity().getRecycleValue())
                .imageUrl(card.getBaseCard().getImageUrl())
                .statHp(card.getStatHp())
                .statAtk(card.getStatAtk())
                .statDef(card.getStatDef())
                .statSpd(card.getStatSpd())
                .obtainedAt(card.getObtainedAt())
                .build();
    }
}
