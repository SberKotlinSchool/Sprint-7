--liquibase formatted sql

--changeset iedenisov:add_index

CREATE INDEX account1_idx_id ON account1 (id);