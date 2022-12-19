--liquibase formatted sql

--changeset kulinichroman:generateData

insert into account1 (id, amount, version)
values (1, 100, 0),
       (2, 200, 0),
       (3, 300, 0)