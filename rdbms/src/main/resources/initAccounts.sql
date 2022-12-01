--liquibase formatted sql

--changeset michael:initAccounts

insert into account(id, amount, version)
values (1, 1000, 0), (2, 0, 0)


