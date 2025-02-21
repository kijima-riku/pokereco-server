INSERT INTO users (user_key) VALUES
    ('550e8400-e29b-41d4-a716-446655440000'),
    ('550e8400-e29b-41d4-a716-446655440001');

INSERT INTO tokens (user_id, access_token, refresh_token) VALUES
    (1, 'access_token_123456', 'refresh_token_123456'),
    (1, 'access_token_abcdef', 'refresh_token_abcdef'),
    (2, 'access_token_xyz789', 'refresh_token_xyz789');

INSERT INTO decks (name) VALUES
    ('クレセリア'),
    ('セレビィ'),
    ('パルキア');

INSERT INTO results (user_id, my_deck, opponent_deck, is_first, turn_count, outcome) VALUES
    (1, 1, 2, TRUE, 10, 0),
    (2, 2, 3, FALSE, 15, 2);

INSERT INTO favorite_decks (user_id, deck_id) VALUES
    (1, 1),
    (2, 3);
