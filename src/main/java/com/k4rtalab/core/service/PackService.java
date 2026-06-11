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

    private static final int PACK_COST = 100;

    @Transactional
    public List<PlayerCard> openPack(UUID playerId, UUID packTypeId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + playerId));

        PackType packType = packTypeRepository.findById(packTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Pack type not found: " + packTypeId));

        if (player.getCoins() < PACK_COST) throw new K4rtaException("Not enough coins to open pack");

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
        List<PlayerCard> generarCards = new ArrayList<>();
        for (int i = 0; i < packType.getCardCount(); i++) {
            PackTypeCard picked = pool.get(rng.nextInt(pool.size()));
            BaseCard baseCard = picked.getBaseCard();

            PlayerCard playerCard = PlayerCard.builder()
                    .owner(player)
                    .baseCard(baseCard)
                    .packSeed(seed)
                    .statHp(baseCard.getStatHpBase() + rng.nextInt(baseCard.getStatHpRange() + 1))
                    .statAtk(baseCard.getStatAtkBase() + rng.nextInt(baseCard.getStatAtkRange() + 1))
                    .statDef(baseCard.getStatDefBase() + rng.nextInt(baseCard.getStatDefRange() + 1))
                    .statSpd(baseCard.getStatSpdBase() + rng.nextInt(baseCard.getStatSpdRange() + 1))
                    .build();

            generarCards.add(playerCard);
        }

        playerCardRepository.saveAll(generarCards);

        // Descontar monedas
        player.setCoins(player.getCoins() - PACK_COST);
        playerRepository.save(player);

        return generarCards;
    }
}