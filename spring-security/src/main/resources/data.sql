INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER'), (3, 'ROLE_API'), (4, 'ROLE_DELETE');

--supe supeUser
INSERT INTO users (id, login, password)
VALUES (1, 'supe', '$2a$10$Kv/FQDx1sBNXPdMt.9ETL.QXTiNS18Ol6dFp58v9H7FVmRyNxmEeG');

--user2 user2
INSERT INTO users (id, login, password)
VALUES (2, 'user2', '$2a$10$uSqeH83MrAAdIU.1nuiS1.KuyObFjsLUKXw/5VQhRnWaxaJ66ZyZm');

--admin admin
INSERT INTO users (id, login, password)
VALUES (3, 'admin', '$2a$10$MrgpCSxnuxNKWV7K6CsLC.18hXJDcNsQasvUZrt1P/Eo23zlZpxvu');

--adminapi admin
INSERT INTO users (id, login, password)
VALUES (4, 'adminapi', '$2a$10$MrgpCSxnuxNKWV7K6CsLC.18hXJDcNsQasvUZrt1P/Eo23zlZpxvu');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 2), (1, 4), (1, 2), (3, 1), (4, 1), (4, 3);

INSERT INTO record (id, name, phone, address) VALUES
                                                              (-1, 'qwe', '23534542', 'wqewqeqw'),
                                                              (-2, 'trewt', '5465436', 'ghgfdhg'),
                                                              (-3, 'qwewq', '6543654', 'hgfhgf'),
                                                              (-4, 'fdsfds', '435435', 'sdfsdfds');