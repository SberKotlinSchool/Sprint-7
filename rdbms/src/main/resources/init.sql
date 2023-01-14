--liquibase formatted sql

--changeset rrmasgutov:init

create table accounts
(
    id bigserial constraint account_pk primary key,
    amount int ,--constraint positive_amount CHECK (amount>0),
    version int
);


INSERT INTO accounts (amount, version) VALUES (10, 0);
INSERT INTO accounts (amount, version) VALUES (8, 0);


