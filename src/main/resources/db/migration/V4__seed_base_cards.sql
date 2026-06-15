-- Base cards seed
DO
$$
    DECLARE
        pack_basic_id UUID := '5934ca92-b531-456d-ae75-4a0ea3d58ef9';
        card_ignis    UUID := gen_random_uuid();
        card_aqua     UUID := gen_random_uuid();
        card_terra    UUID := gen_random_uuid();
        card_ventus   UUID := gen_random_uuid();
        card_umbra    UUID := gen_random_uuid();
        card_lux      UUID := gen_random_uuid();
        card_ferrum   UUID := gen_random_uuid();
        card_tonitrus UUID := gen_random_uuid();
    BEGIN

        INSERT INTO base_cards (id, name, stat_hp_min, stat_hp_avg, stat_hp_max,
                                stat_atk_min, stat_atk_avg, stat_atk_max,
                                stat_def_min, stat_def_avg, stat_def_max,
                                stat_spd_min, stat_spd_avg, stat_spd_max,
                                image_url, theme, contributed_by)
        VALUES (card_ignis, 'Ignis', 40, 55, 70, 60, 75, 90, 20, 30, 40, 50, 60, 75, null, 'mythology', null),
               (card_aqua, 'Aqua', 60, 75, 90, 30, 45, 60, 50, 65, 80, 40, 55, 70, null, 'mythology', null),
               (card_terra, 'Terra', 80, 95, 110, 40, 55, 70, 70, 85, 100, 20, 30, 40, null, 'mythology', null),
               (card_ventus, 'Ventus', 30, 45, 60, 50, 65, 80, 20, 30, 40, 80, 95, 110, null, 'mythology', null),
               (card_umbra, 'Umbra', 50, 65, 80, 70, 85, 100, 40, 55, 70, 60, 75, 90, null, 'mythology', null),
               (card_lux, 'Lux', 55, 70, 85, 55, 70, 85, 55, 70, 85, 55, 70, 85, null, 'mythology', null),
               (card_ferrum, 'Ferrum', 70, 85, 100, 65, 80, 95, 80, 95, 110, 25, 35, 45, null, 'mythology', null),
               (card_tonitrus, 'Tonitrus', 45, 60, 75, 80, 95, 110, 30, 45, 60, 70, 85, 100, null, 'mythology', null);

        INSERT INTO pack_type_cards (pack_type_id, base_card_id)
        VALUES (pack_basic_id, card_ignis),
               (pack_basic_id, card_aqua),
               (pack_basic_id, card_terra),
               (pack_basic_id, card_ventus),
               (pack_basic_id, card_umbra),
               (pack_basic_id, card_lux),
               (pack_basic_id, card_ferrum),
               (pack_basic_id, card_tonitrus);

    END
$$;