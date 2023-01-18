--liquibase formatted sql

--changeset vadim991-tt:indexes
CREATE INDEX account1_index ON account1 ("version", id);
