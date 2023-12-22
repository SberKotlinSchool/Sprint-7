--liquibase formatted sql

--changeset msastashkin:data

INSERT INTO account1
VALUES (1, 500, 0),
       (2, 1000, 0)