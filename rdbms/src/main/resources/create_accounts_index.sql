--liquibase formatted sql

--changeset firebat2:create_accounts_index

CREATE INDEX IF NOT EXISTS amount_index ON accounts.account (amount);