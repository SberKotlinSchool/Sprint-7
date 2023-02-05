--liquibase formatted sql

--changeset vorotovav:init

create table account1
(
    id bigserial constraint account_pk primary key,
    amount int not null check (amount >= 0),
    version int
);

INSERT INTO account1 (id, amount, version) VALUES (1, 100, 1),(2, 100, 1);