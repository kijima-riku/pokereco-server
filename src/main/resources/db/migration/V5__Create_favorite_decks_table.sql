CREATE TABLE IF NOT EXISTS favorite_decks (
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT NOT NULL UNIQUE,
    deck_id    INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_fav_decks_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_fav_decks_deck
        FOREIGN KEY (deck_id) REFERENCES decks(id) ON DELETE CASCADE
);

CREATE TRIGGER trg_fav_decks_updated_at
BEFORE UPDATE ON favorite_decks
FOR EACH ROW
EXECUTE FUNCTION fn_update_updated_at();