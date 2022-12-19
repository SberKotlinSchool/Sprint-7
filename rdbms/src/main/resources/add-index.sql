--liquibase formatted sql

--changeset kulinichroman:addIndex

CREATE INDEX id_index ON account1 USING HASH (id);