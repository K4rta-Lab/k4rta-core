package com.k4rtalab.core.service;

import com.k4rtalab.core.domain.*;
import com.k4rtalab.core.exception.K4rtaException;
import com.k4rtalab.core.exception.ResourceNotFoundException;
import com.k4rtalab.core.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PackService {

    private final PackTypeRepository packTypeRepository;
    private final PackTypeCardRepository packTypeCardRepository;
    private final PlayerCardRepository playerCardRepository;
    private final PackOpenRepository packOpenRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public List<PlayerCard> openPack(UUID playerId, UUID packTypeId) {
        // TODO: add login for 24h reward

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + playerId));

        PackType packType = packTypeRepository.findById(packTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Pack type not found: " + packTypeId));

        if (player.getCoins() < packType.getCost()) throw new K4rtaException("Not enough coins to open pack");

        List<PackTypeCard> pool = packTypeCardRepository.findByPackTypeId(packTypeId);
        if (pool.isEmpty()) throw new ResourceNotFoundException("No cards available in this pack type");

        long seed = new Random().nextLong();
        Random rng = new Random(seed);

        // Registrar paquete abierto
        PackOpen packOpen = PackOpen.builder()
                .player(player)
                .packType(packType)
                .seed(seed)
                .build();
        packOpenRepository.save(packOpen);

        // Generar cartas
        // Todo: reemplazar placeholder con binario de rust
        List<PackTypeRarityWeight> weights = packType.getRarityWeights();
        List<PlayerCard> generarCards = new ArrayList<>();

        for (int i = 0; i < packType.getCardCount(); i++) {
            PackTypeCard picked = pool.get(rng.nextInt(pool.size()));
            BaseCard baseCard = picked.getBaseCard();
            Rarity rarity = pickRarity(weights, rng);
            double multiplier = rarity.getMultiplier().doubleValue();

            PlayerCard playerCard = PlayerCard.builder()
                    .owner(player)
                    .baseCard(baseCard)
                    .rarity(rarity)
                    .packSeed(seed)
                    .statHp(calcStat(baseCard.getStatHpMin(), baseCard.getStatHpAvg(), baseCard.getStatHpMax(), multiplier, rng))
                    .statAtk(calcStat(baseCard.getStatAtkMin(), baseCard.getStatAtkAvg(), baseCard.getStatAtkMax(), multiplier, rng))
                    .statDef(calcStat(baseCard.getStatDefMin(), baseCard.getStatDefAvg(), baseCard.getStatDefMax(), multiplier, rng))
                    .statSpd(calcStat(baseCard.getStatSpdMin(), baseCard.getStatSpdAvg(), baseCard.getStatSpdMax(), multiplier, rng))
                    .build();

            generarCards.add(playerCard);
        }

        playerCardRepository.saveAll(generarCards);

        // Descontar monedas
        player.setCoins(player.getCoins() - packType.getCost());
        playerRepository.save(player);

        return generarCards;
    }

    private Rarity pickRarity(List<PackTypeRarityWeight> weights, Random rng) {
        double roll = rng.nextDouble();
        double cumulative = 0.0;
        for (PackTypeRarityWeight w : weights) {
            cumulative += w.getWeight().doubleValue();
            if (roll < cumulative) return w.getRarity();
        }
        return weights.getLast().getRarity(); // fallback por redondeo
    }

    private int calcStat(int min, int avg, int max, double multiplier, Random rng) {
        // Campana simple con 3 tiradas (aproximación de normal)
        double roll = (rng.nextInt(max - min + 1) +
                rng.nextInt(max - min + 1) +
                rng.nextInt(max - min + 1)) / 3.0 + min;
        return (int) Math.round(roll * multiplier);
    }
}