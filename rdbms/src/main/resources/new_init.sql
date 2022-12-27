--liquibase formatted sql

--changeset goshiq:init

create table account1
(
    id bigserial constraint account_pk primary key,
    amount int CHECK (amount >= 0),
    version int
);

INSERT INTO account1
VALUES  (0, 1000, 1),
        (1, 1000, 1);
