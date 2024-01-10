--liquibase formatted sql

--changeset gagarin:add_id_account_index

create index acc_idx_id on account1 (id);