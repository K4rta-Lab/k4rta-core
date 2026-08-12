package com.k4rtalab.core.mapper;

import com.k4rtalab.core.domain.BaseCard;
import com.k4rtalab.core.dto.response.BaseCardResponse;
import com.k4rtalab.core.dto.response.BaseCardSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BaseCardMapper {

    @Mapping(source = "name", target = "cardName")
    @Mapping(source = "rarity.name", target = "rarity")
    BaseCardSummaryResponse toSummaryResponse(BaseCard card);

    @Mapping(source = "name", target = "cardName")
    @Mapping(source = "rarity.name", target = "rarity")
    BaseCardResponse toResponse(BaseCard card);

    List<BaseCardSummaryResponse> toSummaryResponses(List<BaseCard> cards);
}