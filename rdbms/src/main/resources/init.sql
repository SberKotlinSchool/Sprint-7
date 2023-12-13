--liquibase formatted sql

--changeset rrmasgutov:init

CREATE TABLE account1
(
    id      bigserial
        CONSTRAINT account_pk PRIMARY KEY,
    amount  INT,
    version INT
);


