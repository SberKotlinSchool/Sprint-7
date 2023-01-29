INSERT INTO users (username, password, groups, cred_expired, enabled, expired, locked)
VALUES ('admin', '{bcrypt}$2a$14$z.JxbUf1rhEyf0L8G1Jpw.6yhyGEXMZImuwlJ7RkIAODT/JLSbWNS', 'ROLE_ADMIN', false, true,
        false, false),
       ('user', '{bcrypt}$2a$14$ZKENxlJSC8eUOiw0F.HC2uyxFV1Mp680kgVDGEJ3KezUErHiifdea', 'ROLE_ADMIN', false, true,
        false, false);

INSERT INTO addresses (name, city, phone)
VALUES ('Eric Cartman', 'South Park', '423523'),
       ('Philip J. Fry', 'New York', '0412314'),
       ('Justin Peter Griffin', 'Quahog', '031994'),
       ('Steve Williams', 'Brickleberry', '222012')
