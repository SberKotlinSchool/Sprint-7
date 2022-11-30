--liquibase formatted sql

--changeset valentina:addIndex

CREATE INDEX id_index ON account1 USING HASH (id);
