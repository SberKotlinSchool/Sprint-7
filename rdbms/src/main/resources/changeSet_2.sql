--liquibase formatted sql

--changeset max:init

CREATE INDEX account1_index ON account1 ("version",id);