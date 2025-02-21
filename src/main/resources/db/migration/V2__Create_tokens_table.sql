CREATE TABLE IF NOT EXISTS tokens (
    id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id         INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    access_token    UUID NOT NULL,
    refresh_token   UUID NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);