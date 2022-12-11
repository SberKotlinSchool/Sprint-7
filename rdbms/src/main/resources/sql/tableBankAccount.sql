--liquibase formatted sql

--changeset Andrey Imshenik:create table иankAccount

CREATE TABLE BankAccount(
    id serial constraint bank_account_pk primary key,
    owner TEXT not null ,
    amount int NOT NULL CHECK (amount >= 0),
    version int NOT NULL
);


INSERT INTO BankAccount (id, owner, amount, version) VALUES (1, 'Owner-1',100, 1),(2, 'Owner-2', 100, 1);