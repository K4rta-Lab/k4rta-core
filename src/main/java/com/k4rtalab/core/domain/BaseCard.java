package com.k4rtalab.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rarity_id", nullable = false)
    private Rarity rarity;

    @PositiveOrZero
    @Column(name = "stat_hp_base", nullable = false)
    private int statHpBase;

    @PositiveOrZero
    @Column(name = "stat_hp_range", nullable = false)
    private int statHpRange;

    @PositiveOrZero
    @Column(name = "stat_atk_base", nullable = false)
    private int statAtkBase;

    @PositiveOrZero
    @Column(name = "stat_atk_range", nullable = false)
    private int statAtkRange;

    @PositiveOrZero
    @Column(name = "stat_def_base", nullable = false)
    private int statDefBase;

    @PositiveOrZero
    @Column(name = "stat_def_range", nullable = false)
    private int statDefRange;

    @PositiveOrZero
    @Column(name = "stat_spd_base", nullable = false)
    private int statSpdBase;

    @PositiveOrZero
    @Column(name = "stat_spd_range", nullable = false)
    private int statSpdRange;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "theme")
    private String theme;

    @Column(name = "contributed_by")
    private String contributedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}