--liquibase formatted sql

--changeset gaponchukAG:createAccountTable.sql

create table accounts
(
    ID INT NOT NULL,
    ACCOUNT_NAME text,
    AMOUNT INT CHECK(amount > 0),
    VERSION INT
);
insert into accounts values (1, 'Account1', 2000,1);
insert into accounts values (2, 'Account2', 1000, 1);