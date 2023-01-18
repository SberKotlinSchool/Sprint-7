--liquibase formatted sql

--changeset vadim991-tt:data
INSERT INTO account1 (id, amount, version)
VALUES (1, 100, 0),
       (2, 200, 0)
