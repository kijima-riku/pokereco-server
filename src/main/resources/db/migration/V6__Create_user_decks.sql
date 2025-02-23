CREATE TABLE IF NOT EXISTS user_decks (
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    INT NOT NULL,
    deck_id    INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_user_decks_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_user_decks_deck
        FOREIGN KEY (deck_id) REFERENCES decks(id) ON DELETE CASCADE
);