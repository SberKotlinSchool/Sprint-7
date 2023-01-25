--liquibase formatted sql

--changeset max:init

create table account1
(
    id bigserial constraint account1_pk primary key,
    amount int CHECK (amount >= 0),
    version int
);

INSERT INTO account1 (id, amount, version)
VALUES (1, 300, 0),
       (2, 200, 0);