package com.k4rtalab.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "base_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @PositiveOrZero
    @Column(name = "stat_glamour", nullable = false)
    private int statGlamour;

    @PositiveOrZero
    @Column(name = "stat_shade", nullable = false)
    private int statShade;

    @PositiveOrZero
    @Column(name = "stat_energy", nullable = false)
    private int statEnergy;

    @ManyToOne
    @JoinColumn(name = "rarity_id")
    private Rarity rarity;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "contributed_by")
    private String contributedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}