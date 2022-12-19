--liquibase formatted sql

--changeset kulinichroman:init

create table account1
(
    id bigserial constraint account_pk primary key,
    amount int constraint check_amount check (amount >= 0),
    version int
);


