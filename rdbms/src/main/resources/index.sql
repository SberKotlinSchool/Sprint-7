--liquibase formatted sql

--changeset umka25:index

CREATE UNIQUE INDEX IF NOT EXISTS account1_id_idx ON account1 (id);