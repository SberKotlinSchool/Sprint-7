--liquibase formatted sql

--changeset gaponchukAG:createAccountIndex.sql

CREATE UNIQUE index account_table_index ON accounts using btree (id)

