--liquibase formatted sql

--changeset goshiq:new_index

CREATE INDEX amount_index ON account1 (amount);