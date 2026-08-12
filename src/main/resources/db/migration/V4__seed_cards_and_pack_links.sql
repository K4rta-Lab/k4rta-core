-- V4__seed_cards_and_pack_links.sql

WITH basic_cards AS (
    INSERT INTO base_cards (id, name, stat_glamour, stat_shade, stat_energy, rarity_id, slug, contributed_by)
        VALUES
            (gen_random_uuid(), 'Pepi Basic', 40, 15, 1, (SELECT id FROM rarities WHERE name = 'local'), 'pepi-basic', 'k4rta-team'),
            (gen_random_uuid(), 'Manoli del Barrio', 35, 20, 2, (SELECT id FROM rarities WHERE name = 'local'), 'manoli-del-barrio', 'k4rta-team'),
            (gen_random_uuid(), 'Toñi Excel', 45, 10, 1, (SELECT id FROM rarities WHERE name = 'local'), 'toni-excel', 'k4rta-team'),
            (gen_random_uuid(), 'Lady Drama', 120, 85, 5, (SELECT id FROM rarities WHERE name = 'all-star'), 'lady-drama', 'k4rta-team'),
            (gen_random_uuid(), 'Reina del Chotis', 110, 70, 4, (SELECT id FROM rarities WHERE name = 'all-star'), 'reina-del-chotis', 'k4rta-team')
        RETURNING id
),
     gold_cards AS (
         INSERT INTO base_cards (id, name, stat_glamour, stat_shade, stat_energy, rarity_id, slug, contributed_by)
             VALUES
                 (gen_random_uuid(), 'Miss Croqueta', 90, 45, 3, (SELECT id FROM rarities WHERE name = 'iconic'), 'miss-croqueta', 'k4rta-team'),
                 (gen_random_uuid(), 'Fabiola de Feria', 95, 50, 3, (SELECT id FROM rarities WHERE name = 'iconic'), 'fabiola-de-feria', 'k4rta-team'),
                 (gen_random_uuid(), 'Susi Purpurina', 85, 55, 4, (SELECT id FROM rarities WHERE name = 'iconic'), 'susi-purpurina', 'k4rta-team')
             RETURNING id
     ),
     premium_cards AS (
         INSERT INTO base_cards (id, name, stat_glamour, stat_shade, stat_energy, rarity_id, slug, contributed_by)
             VALUES
                 (gen_random_uuid(), 'Giralda Glam', 150, 60, 4, (SELECT id FROM rarities WHERE name = 'supreme'), 'giralda-glam', 'k4rta-team'),
                 (gen_random_uuid(), 'Emperatriz Sevillana', 160, 70, 5, (SELECT id FROM rarities WHERE name = 'supreme'), 'emperatriz-sevillana', 'k4rta-team')
             RETURNING id
     )
INSERT INTO pack_type_cards (base_card_id, pack_type_id)
SELECT id, (SELECT id FROM pack_types WHERE name = 'BASIC') FROM basic_cards
UNION ALL
SELECT id, (SELECT id FROM pack_types WHERE name = 'GOLD') FROM gold_cards
UNION ALL
SELECT id, (SELECT id FROM pack_types WHERE name = 'PREMIUM') FROM premium_cards;