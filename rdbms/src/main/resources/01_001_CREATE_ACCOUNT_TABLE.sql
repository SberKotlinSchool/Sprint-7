--liquibase formatted sql

--changeset id:01_001_01_CREATE_ACCOUNT_TABLE author:aisypchenko
CREATE TABLE IF NOT EXISTS account
(
    id bigserial constraint account_pk primary key,
    amount bigint CONSTRAINT positive_amount CHECK (amount > 0),
    version int
);
--rollback drop table account

--changeset id:01_001_02_CREATE_INDEX
CREATE UNIQUE INDEX idx_version_id ON account(version, id);
--rollback DROP INDEX idx_number