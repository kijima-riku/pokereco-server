INSERT INTO users (user_key) VALUES
    ('780f1914-87b7-4f39-8f24-350276eb2038'),
    ('4e98dd0a-b86e-4268-8ee3-02618c45ec10');

INSERT INTO tokens (user_id, access_token, refresh_token) VALUES
    (1, 'd32ec933-4735-487d-8b83-4c90bcd0b913', 'f75da770-32ed-41d9-b4c2-e7739c471203'),
    (1, 'd3de7110-7020-4397-a79f-1295bba1f91b', '94e1ca4f-f74f-49a4-8964-7586aec73a8e'),
    (2, 'b50798d2-1b62-408e-8d98-b75ce83573bb', 'e74d60e0-f52f-460e-b388-4593c425ce37');

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
