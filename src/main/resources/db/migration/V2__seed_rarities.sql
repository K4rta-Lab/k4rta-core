INSERT INTO rarities (id, name, tier, multiplier, recycle_value) VALUES
(gen_random_uuid(), 'local',     1, 1.00,  5),
(gen_random_uuid(), 'trending',   2, 1.30, 15),
(gen_random_uuid(), 'iconic',       3, 1.80, 40),
(gen_random_uuid(), 'all-star',  4, 2.50, 120),
(gen_random_uuid(), 'supreme',     5, 3.50, 400);