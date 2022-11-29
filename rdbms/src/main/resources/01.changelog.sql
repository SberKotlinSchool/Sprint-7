--liquibase formatted sql

--changeset synkovav:01

INSERT INTO account1 (id, amount, version)
VALUES (1, 200, 0),
(2, 100, 0)