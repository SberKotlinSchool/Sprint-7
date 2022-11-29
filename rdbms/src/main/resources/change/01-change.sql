--liquibase formatted sql

--changeset yakovlev:01-change

INSERT INTO account1 (id, amount, version)
VALUES (1, 1000, 0),
       (2, 500, 0)