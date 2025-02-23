CREATE TABLE IF NOT EXISTS results (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    my_deck        INT NOT NULL,
    opponent_deck  INT NOT NULL,
    is_first       BOOLEAN NOT NULL,
    turn_count     SMALLINT NOT NULL,
    outcome        SMALLINT NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_results_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_results_my_deck
        FOREIGN KEY (my_deck) REFERENCES decks(id) ON DELETE CASCADE,

    CONSTRAINT fk_results_opponent_deck
        FOREIGN KEY (opponent_deck) REFERENCES decks(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION fn_update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_results_updated_at
BEFORE UPDATE ON results
FOR EACH ROW
EXECUTE FUNCTION fn_update_updated_at();