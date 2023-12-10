--liquibase formatted sql

--changeset dakorshik:add_account_id_index

create index acc_index on account1 (id);