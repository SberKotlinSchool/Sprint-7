--liquibase formatted sql

--changeset kslabko:add_primary_key

alter table account1 add constraint account_pk primary key (id);