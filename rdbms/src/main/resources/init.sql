--liquibase formatted sql

--changeset dpafonyushkin:init

create table account1
(
    id bigserial constraint account_pk primary key,
    amount int,
    version int
);


