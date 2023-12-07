--liquibase formatted sql

--changeset ikompleev:insertData
insert into account values (1, 1000, 0);
insert into account values (2, 2000, 0);