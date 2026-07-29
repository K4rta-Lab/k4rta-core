INSERT INTO rarities (id, name, tier, multiplier, recycle_value) VALUES
(gen_random_uuid(), 'COMMON',     1, 1.00,  5),
(gen_random_uuid(), 'UNCOMMON',   2, 1.30, 15),
(gen_random_uuid(), 'RARE',       3, 1.80, 40),
(gen_random_uuid(), 'LEGENDARY',  4, 2.50, 120),
(gen_random_uuid(), 'MYTHIC',     5, 3.50, 400);