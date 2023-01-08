--liquibase formatted sql

--changeset kslabko:add_data_to_table

insert into account1 (id, amount, version) values (1, 1000, 1);
insert into account1 (id, amount, version) values (2, 2000, 1);

