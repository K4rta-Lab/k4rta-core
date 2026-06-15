package com.k4rtalab.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "player_cards", indexes = @Index(name = "idx_player_cards_owner", columnList = "owner_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Player owner;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_card_id", nullable = false)
    private BaseCard baseCard;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rarity_id", nullable = false)
    private Rarity rarity;

    @Min(0)
    @Column(name = "stat_hp", nullable = false)
    private int statHp;

    @Min(0)
    @Column(name = "stat_atk", nullable = false)
    private int statAtk;

    @Min(0)
    @Column(name = "stat_def", nullable = false)
    private int statDef;

    @Min(0)
    @Column(name = "stat_spd", nullable = false)
    private int statSpd;

    @Column(name = "pack_seed")
    private Long packSeed;

    @CreationTimestamp
    @Column(name = "obtained_at", updatable = false)
    private LocalDateTime obtainedAt;
}