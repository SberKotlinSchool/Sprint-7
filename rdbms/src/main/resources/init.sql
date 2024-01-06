--liquibase formatted sql

--changeset avkononenko:init

create table account1
(
    id bigserial constraint account_pk primary key,
    amount int check ( amount >= 0 ),
    version int
);

insert into account1 (amount, version) values
                         (500, 1),
                         (100, 1);


