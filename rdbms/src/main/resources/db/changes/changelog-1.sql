--liquibase formatted sql

--changeset avlalekhin:1:table

create table account
(
    id bigserial constraint account_primary_key primary key,
    amount int constraint non_negative_amount check(amount >= 0),
    version int
);

--changeset avlalekhin:2:index

create index account_index on account(id);
