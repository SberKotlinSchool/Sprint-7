--liquibase formatted sql

--changeset synkovav:02

CREATE INDEX account1_index ON account1 ("version",id);