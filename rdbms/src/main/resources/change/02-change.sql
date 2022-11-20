--liquibase formatted sql

--changeset yakovlev:02-change

CREATE INDEX account1_version_idx ON account1 ("version",id);