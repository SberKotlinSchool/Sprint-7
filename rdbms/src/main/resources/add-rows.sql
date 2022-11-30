--liquibase formatted sql

--changeset valentina:add

insert into account1 (id, amount, version)
values (1, 800, 0), (2, 100, 0), (3, 500, 0)