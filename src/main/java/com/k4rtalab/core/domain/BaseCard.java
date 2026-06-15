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
    @Column(name = "stat_hp_min", nullable = false)
    private int statHpMin;

    @PositiveOrZero
    @Column(name = "stat_hp_avg", nullable = false)
    private int statHpAvg;

    @PositiveOrZero
    @Column(name = "stat_hp_max", nullable = false)
    private int statHpMax;

    @PositiveOrZero
    @Column(name = "stat_atk_min", nullable = false)
    private int statAtkMin;

    @PositiveOrZero
    @Column(name = "stat_atk_avg", nullable = false)
    private int statAtkAvg;

    @PositiveOrZero
    @Column(name = "stat_atk_max", nullable = false)
    private int statAtkMax;

    @PositiveOrZero
    @Column(name = "stat_def_min", nullable = false)
    private int statDefMin;

    @PositiveOrZero
    @Column(name = "stat_def_avg", nullable = false)
    private int statDefAvg;

    @PositiveOrZero
    @Column(name = "stat_def_max", nullable = false)
    private int statDefMax;

    @PositiveOrZero
    @Column(name = "stat_spd_min", nullable = false)
    private int statSpdMin;

    @PositiveOrZero
    @Column(name = "stat_spd_avg", nullable = false)
    private int statSpdAvg;

    @PositiveOrZero
    @Column(name = "stat_spd_max", nullable = false)
    private int statSpdMax;

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