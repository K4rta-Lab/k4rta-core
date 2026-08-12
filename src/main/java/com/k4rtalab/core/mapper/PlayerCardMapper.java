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
    @Mapping(source = "baseCard.rarity.name", target = "rarity")
    @Mapping(source = "baseCard.rarity.recycleValue", target = "recycleValue")
    @Mapping(source = "baseCard.slug", target = "slug")
    @Mapping(source = "baseCard.statGlamour", target = "statGlamour")
    @Mapping(source = "baseCard.statShade", target = "statShade")
    @Mapping(source = "baseCard.statEnergy", target = "statEnergy")
    PlayerCardResponse toCardResponse(PlayerCard card);

    @Mapping(source = "baseCard.name", target = "cardName")
    @Mapping(source = "baseCard.rarity.name", target = "rarity")
    @Mapping(source = "baseCard.slug", target = "slug")
    @Mapping(source = "baseCard.statGlamour", target = "statGlamour")
    @Mapping(source = "baseCard.statShade", target = "statShade")
    @Mapping(source = "baseCard.statEnergy", target = "statEnergy")
    CardCollectionItemResponse toCollectionItem(PlayerCard card);

    List<PlayerCardResponse> toResponseList(List<PlayerCard> cards);

    List<CardCollectionItemResponse> toCollectionItems(List<PlayerCard> cards);
}
