INSERT INTO TOKENS (token) VALUES
('abcd-efgh-ijkl-1234'),
('mnop-qrst-uvwx-5678');

INSERT INTO DECKS (name) VALUES
('クレセリア'),
('セレビィ'),
('パルキア');

INSERT INTO RESULTS (user_id, my_deck, opponent_deck, is_first, turn_count, outcome) VALUES
(1, 1, 2, TRUE, 10, 0),
(2, 2, 3, FALSE, 15, 2);

INSERT INTO FAVORITE_DECKS (user_id, deck_id) VALUES
(1, 1),
(2, 3);
