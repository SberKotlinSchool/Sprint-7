--liquibase formatted sql

-- changeset firebat2:create_account_table
CREATE SCHEMA IF NOT EXISTS accounts;

DROP TABLE IF EXISTS accounts.account;

CREATE TABLE accounts.account
(
    id      bigserial
        CONSTRAINT account_pk PRIMARY KEY,
    amount  int check (amount >= 0),
    version int
);