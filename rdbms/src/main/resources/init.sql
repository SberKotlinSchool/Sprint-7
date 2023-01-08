--liquibase formatted sql

--changeset rrmasgutov:init

create table account1
(
    id bigserial,
    amount int check ( amount >= 0 ), -- Важный момент: **сумма на счете не может уходить в минус**.
    version int
);
