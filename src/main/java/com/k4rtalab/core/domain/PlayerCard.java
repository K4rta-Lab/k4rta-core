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

    @PositiveOrZero
    @Column(name = "ev_hp", nullable = false)
    @Builder.Default
    private int evHp = 0;

    @PositiveOrZero
    @Column(name = "ev_atk", nullable = false)
    @Builder.Default
    private int evAtk = 0;

    @PositiveOrZero
    @Column(name = "ev_def", nullable = false)
    @Builder.Default
    private int evDef = 0;

    @PositiveOrZero
    @Column(name = "ev_spd", nullable = false)
    @Builder.Default
    private int evSpd = 0;

    @Column(name = "pack_seed")
    private Long packSeed;

    @CreationTimestamp
    @Column(name = "obtained_at", updatable = false)
    private LocalDateTime obtainedAt;

    public int getTotalEvPoints() {
        return evHp + evAtk + evDef + evSpd;
    }
}