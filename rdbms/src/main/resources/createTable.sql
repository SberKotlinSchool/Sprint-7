--liquibase formatted sql

--changeset michael:createTable

create table account
(
    id bigserial constraint account_pk primary key,
    amount int CHECK (amount >= 0),
    version int
);