--liquibase formatted sql

--changeset gagarin:add_id_account_index

create index acc_index on account1 (id);