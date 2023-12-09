--liquibase formatted sql

--changeset rrmasgutov:init

CREATE TABLE IF NOT EXISTS account1
(
    id BIGSERIAL CONSTRAINT account_pk PRIMARY KEY,
    amount INT CHECK (amount >= 0),
    version INT
);


