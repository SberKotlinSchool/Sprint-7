--liquibase formatted sql

--changeset dokl57:add_index_to_account1_table

CREATE UNIQUE INDEX IF NOT EXISTS account1_id_idx ON account1 (id);
