package com.k4rtalab.core.mapper;

import com.k4rtalab.core.domain.Rarity;
import com.k4rtalab.core.domain.TradeListing;
import com.k4rtalab.core.domain.TradeOffer;
import com.k4rtalab.core.domain.TradeRequest;
import com.k4rtalab.core.dto.response.OfferResponse;
import com.k4rtalab.core.dto.response.TradeListingResponse;
import com.k4rtalab.core.dto.response.TradeRequestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = BaseCardMapper.class)
public interface TradeMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.username", target = "ownerUsername")
    @Mapping(source = "offeredCard", target = "offeredCard")
    @Mapping(source = "wantedCard.id", target = "wantedCardId")
    @Mapping(source = "wantedCard.name", target = "wantedCardName")
    @Mapping(source = "status", target = "status")
    TradeListingResponse toListingResponse(TradeListing listing);

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.username", target = "ownerUsername")
    @Mapping(source = "wantedCard.id", target = "wantedCardId")
    @Mapping(source = "wantedCard.name", target = "wantedCardName")
    @Mapping(source = "status", target = "status")
    TradeRequestResponse toRequestResponse(TradeRequest request);

    @Mapping(source = "request.id", target = "requestId")
    @Mapping(source = "offerer.id", target = "offererId")
    @Mapping(source = "offerer.username", target = "offererUsername")
    @Mapping(source = "offeredCard", target = "offeredCard")
    @Mapping(source = "status", target = "status")
    OfferResponse toOfferResponse(TradeOffer offer);

    List<TradeListingResponse> toListingResponses(List<TradeListing> listings);

    List<TradeRequestResponse> toRequestResponses(List<TradeRequest> requests);

    List<OfferResponse> toOfferResponses(List<TradeOffer> offers);

    default String mapRarityToString(Rarity rarity) {
        return rarity != null ? rarity.getName() : null;
    }
}
