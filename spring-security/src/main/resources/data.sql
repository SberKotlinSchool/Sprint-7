INSERT INTO role (id, name) VALUES
(1, 'ADMIN'),
(2, 'USER'),
(3, 'EDITOR'),
(4, 'API');


INSERT INTO users (id, login, password, role_id) VALUES
(1,'ADMIN','$2a$10$fSG0J9EKGrlmhCJdeNL71e8u2gnsY9wvjTkcEVDgDZy4aFHcDQ2uC',1),
(2,'USER','$2a$10$X.OKZgzPxVeMsF68hpbdSO6DLkOfUgRj8VXmzH85l2Wt/Nhho6Xh.',2),
(3,'EDITOR','$2a$10$pZeMe6MiW0FjBlk6nmebJOzMqw3JftkMSZ0HgHT7gWaMbSJ.qdsOe',3),
(4,'API','$2a$10$sNMaEGnvnYS.WhlV5.zAMOjKypTaYREUFP3CjRqAPSg9zVHSqGrz6',4);