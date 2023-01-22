--liquibase formatted sql

--changeset annapo:index

CREATE INDEX id_index ON accounts USING HASH(id);


