--liquibase formatted sql

--changeset shadowsith:createIndex

CREATE INDEX  index_account_id  ON  account1(id);