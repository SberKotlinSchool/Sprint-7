--liquibase formatted sql

--changeset idromanova:01

INSERT INTO account1 (id, amount, version)
VALUES (1, 900, 0),
       (2, 100, 0)

