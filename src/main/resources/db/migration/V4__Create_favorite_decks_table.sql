CREATE TABLE IF NOT EXISTS FAVORITE_DECKS (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    deck_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fav_decks_user FOREIGN KEY (user_id) REFERENCES TOKENS(id) ON DELETE CASCADE,
    CONSTRAINT fk_fav_decks_deck FOREIGN KEY (deck_id) REFERENCES DECKS(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_updated_at
BEFORE UPDATE ON FAVORITE_DECKS
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
