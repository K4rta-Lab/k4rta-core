package com.k4rtalab.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "rarities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rarity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @PositiveOrZero
    @Column(name = "ev_points", nullable = false)
    private int evPoints;
}