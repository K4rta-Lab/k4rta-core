INSERT INTO pack_types (id, name, description, card_count, cost) VALUES
(gen_random_uuid(), 'BASIC',   'Standard pack, mostly commons',         5, 100),
(gen_random_uuid(), 'GOLD',    'Better odds, more uncommons and rares', 5, 250),
(gen_random_uuid(), 'PREMIUM', 'High odds of rare and legendary cards', 5, 600);

INSERT INTO pack_type_rarity_weights (pack_type_id, rarity_id, weight)
SELECT p.id, r.id,
       CASE p.name
           WHEN 'BASIC' THEN
               CASE r.name
                   WHEN 'local'    THEN 0.6000
                   WHEN 'trending'  THEN 0.3000
                   WHEN 'iconic'      THEN 0.0800
                   WHEN 'all-star' THEN 0.0150
                   WHEN 'supreme'    THEN 0.0050
                   END
           WHEN 'GOLD' THEN
               CASE r.name
                   WHEN 'local'    THEN 0.3500
                   WHEN 'trending'  THEN 0.3500
                   WHEN 'iconic'      THEN 0.2000
                   WHEN 'all-star' THEN 0.0800
                   WHEN 'supreme'    THEN 0.0200
                   END
           WHEN 'PREMIUM' THEN
               CASE r.name
                   WHEN 'local'    THEN 0.1000
                   WHEN 'trending'  THEN 0.2500
                   WHEN 'iconic'      THEN 0.3500
                   WHEN 'all-star' THEN 0.2200
                   WHEN 'supreme'    THEN 0.0800
                   END
           END
FROM pack_types p
         CROSS JOIN rarities r;