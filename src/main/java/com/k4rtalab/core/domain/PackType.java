package com.k4rtalab.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pack_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @PositiveOrZero
    @Column(name = "card_count", nullable = false)
    @Builder.Default
    int cardCount = 5;

    @PositiveOrZero
    @Column(name = "cost", nullable = false)
    private int cost;

    @OneToMany(mappedBy = "packType", fetch = FetchType.LAZY)
    private List<PackTypeRarityWeight> rarityWeights;

    @Column(name = "available_from")
    private LocalDateTime availableFrom;

    @Column(name = "available_until")
    private LocalDateTime availableUntil;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}