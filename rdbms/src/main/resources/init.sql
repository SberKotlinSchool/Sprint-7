--liquibase formatted sql

--changeset firebat2:init

INSERT INTO accounts.account (id, amount, version)
VALUES (1, 1000, 0),
       (2, 1500, 0),
       (3, 9000, 0);