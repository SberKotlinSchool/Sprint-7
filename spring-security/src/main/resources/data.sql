INSERT INTO USERS (CRED_EXPIRED, ENABLED, EXPIRED, LOCKED, LOGIN, PASS, ROLES) VALUES
(false, true, false, false, 'Maria', '$2a$12$2IKBFq5yUZoUiAlyjYhGKeCcz5QY5SO/KYrA7ojSl1w8A6XeodCpa', 'ROLE_ADMIN'),
(false, true, false, false, 'name', '$2a$12$0YF5YYiLD7ONoueDATtvROgkeMiidKy12ugENBrUf7dlqu2pzagES', 'ROLE_USER');

INSERT INTO NOTES (TEXT, AUTHOR) VALUES
('Do not forget to buy milk', 'Maria'),
('One more tip', 'Maria'),
('Useful note', 'Maria'),
('Do some sleep', 'name'),
('Not a useful note', 'name');