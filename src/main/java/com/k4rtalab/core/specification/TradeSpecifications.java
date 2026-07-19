package com.k4rtalab.core.specification;

import com.k4rtalab.core.domain.TradeStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TradeSpecifications {
    private TradeSpecifications() {}

    public static <T> Specification<T> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null
                : cb.equal(root.get("status"), TradeStatus.valueOf(status));
    }

    public static <T> Specification<T> hasWantedBaseCard(UUID cardId) {
        return (root, query, cb) -> cardId == null ? null
                : cb.equal(root.get("wantedBaseCard").get("id"), cardId);
    }

    public static <T> Specification<T> hasOwner(UUID ownerId) {
        return (root, query, cb) -> ownerId == null ? null
                : cb.equal(root.get("owner").get("id"), ownerId);
    }
}
