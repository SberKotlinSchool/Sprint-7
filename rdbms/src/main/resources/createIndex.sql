--liquibase formatted sql

--changeset michael:createIndex

CREATE INDEX account_version_idx ON account ("id", "version");