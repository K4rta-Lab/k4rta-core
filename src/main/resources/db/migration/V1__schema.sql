CREATE TYPE trade_status AS ENUM ('OPEN', 'COMPLETED', 'CANCELLED');
CREATE TYPE offer_status AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED');

CREATE TABLE IF NOT EXISTS rarities
(
    id            UUID PRIMARY KEY,
    name          VARCHAR UNIQUE NOT NULL,
    multiplier    DECIMAL(4, 2)  NOT NULL,
    recycle_value INTEGER        NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id          UUID PRIMARY KEY,
    username    VARCHAR UNIQUE NOT NULL,
    email       VARCHAR UNIQUE NOT NULL,
    password    VARCHAR        NOT NULL,
    coins       INTEGER   DEFAULT 0,
    last_reward TIMESTAMP DEFAULT '2000-01-01',
    created_at  TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS base_cards
(
    id             UUID PRIMARY KEY,
    name           VARCHAR NOT NULL,
    stat_hp_min    INTEGER NOT NULL,
    stat_hp_avg    INTEGER NOT NULL,
    stat_hp_max    INTEGER NOT NULL,
    stat_atk_min   INTEGER NOT NULL,
    stat_atk_avg   INTEGER NOT NULL,
    stat_atk_max   INTEGER NOT NULL,
    stat_def_min   INTEGER NOT NULL,
    stat_def_avg   INTEGER NOT NULL,
    stat_def_max   INTEGER NOT NULL,
    stat_spd_min   INTEGER NOT NULL,
    stat_spd_avg   INTEGER NOT NULL,
    stat_spd_max   INTEGER NOT NULL,
    image_url      VARCHAR,
    theme          VARCHAR,
    contributed_by VARCHAR,
    created_at     TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS pack_types
(
    id              UUID PRIMARY KEY,
    name            VARCHAR UNIQUE NOT NULL,
    description     VARCHAR,
    card_count      INTEGER        NOT NULL DEFAULT 5,
    cost            INTEGER        NOT NULL DEFAULT 25,
    available_from  TIMESTAMP,
    available_until TIMESTAMP,
    created_at      TIMESTAMP               DEFAULT now()
);

CREATE TABLE IF NOT EXISTS pack_type_cards
(
    pack_type_id UUID REFERENCES pack_types (id),
    base_card_id UUID REFERENCES base_cards (id),
    PRIMARY KEY (pack_type_id, base_card_id)
);

CREATE TABLE IF NOT EXISTS pack_type_rarity_weights
(
    pack_type_id UUID REFERENCES pack_types (id),
    rarity_id    UUID REFERENCES rarities (id),
    weight       DECIMAL(5, 4) NOT NULL,
    PRIMARY KEY (pack_type_id, rarity_id)
);

CREATE TABLE IF NOT EXISTS pack_opens
(
    id           UUID PRIMARY KEY,
    player_id    UUID REFERENCES users (id),
    pack_type_id UUID REFERENCES pack_types (id),
    seed         BIGINT NOT NULL,
    opened_at    TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS player_cards
(
    id           UUID PRIMARY KEY,
    owner_id     UUID REFERENCES users (id),
    base_card_id UUID REFERENCES base_cards (id),
    rarity_id    UUID REFERENCES rarities (id),
    stat_hp      INTEGER NOT NULL,
    stat_atk     INTEGER NOT NULL,
    stat_def     INTEGER NOT NULL,
    stat_spd     INTEGER NOT NULL,
    pack_seed    BIGINT,
    obtained_at  TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_player_cards_owner ON player_cards (owner_id);

CREATE TABLE IF NOT EXISTS trade_listings
(
    id                  UUID PRIMARY KEY,
    owner_id            UUID REFERENCES users (id),
    offered_card_id     UUID REFERENCES player_cards (id),
    wanted_base_card_id UUID REFERENCES base_cards (id),
    status              trade_status DEFAULT 'OPEN',
    created_at          TIMESTAMP    DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_listings_status ON trade_listings (status);
CREATE INDEX IF NOT EXISTS idx_listings_wanted ON trade_listings (wanted_base_card_id);

CREATE TABLE IF NOT EXISTS trade_requests
(
    id                  UUID PRIMARY KEY,
    owner_id            UUID REFERENCES users (id),
    wanted_base_card_id UUID REFERENCES base_cards (id),
    status              trade_status DEFAULT 'OPEN',
    created_at          TIMESTAMP    DEFAULT now()
);

CREATE TABLE IF NOT EXISTS trade_offers
(
    id              UUID PRIMARY KEY,
    request_id      UUID REFERENCES trade_requests (id),
    offerer_id      UUID REFERENCES users (id),
    offered_card_id UUID REFERENCES player_cards (id),
    status          offer_status DEFAULT 'PENDING',
    created_at      TIMESTAMP    DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_offers_request ON trade_offers (request_id);