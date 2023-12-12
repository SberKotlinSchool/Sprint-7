--liquibase formatted sql

--changeset rrmasgutov:init

create table account1
(
    id bigserial constraint account_pk primary key,
    amount int CHECK ( amount >= 0 ),
    version int
);


