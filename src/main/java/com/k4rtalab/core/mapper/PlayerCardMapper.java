package com.k4rtalab.core.mapper;

import com.k4rtalab.core.domain.PlayerCard;
import com.k4rtalab.core.dto.model.CardCollectionItemResponse;
import com.k4rtalab.core.dto.response.PlayerCardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface PlayerCardMapper {
    @Mapping(source = "baseCard.id", target = "baseCardId")
    @Mapping(source = "baseCard.name", target = "cardName")
    @Mapping(source = "rarity.name", target = "rarity")
    @Mapping(source = "rarity.recycleValue", target = "recycleValue")
    @Mapping(source = "baseCard.slug", target = "slug")
    PlayerCardResponse toCardResponse(PlayerCard card);

    @Mapping(source = "baseCard.name", target = "cardName")
    @Mapping(source = "rarity.name", target = "rarity")
    @Mapping(source = "baseCard.slug", target = "slug")
    CardCollectionItemResponse toCollectionItem(PlayerCard card);

    List<PlayerCardResponse> toResponseList(List<PlayerCard> cards);

    List<CardCollectionItemResponse> toCollectionItems(List<PlayerCard> cards);
}
