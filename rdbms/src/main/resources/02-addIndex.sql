--liquibase formatted sql

--changeset ikompleev:addIndex
create index account_id_index on account(id);