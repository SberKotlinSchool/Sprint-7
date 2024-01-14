--liquibase formatted sql

--changeset avkononenko:index

create index if not exists account1_index on account1(id);