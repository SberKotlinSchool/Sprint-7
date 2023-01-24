--liquibase formatted sql

--changeset rrmasgutov:init
CREATE TABLE account1
(
    id      BIGSERIAL
        CONSTRAINT account_pk PRIMARY KEY,
    amount  BIGINT,
    version BIGINT
)
;

--changeset astafex:add_index
CREATE INDEX account1_id_index ON account1 (id)

