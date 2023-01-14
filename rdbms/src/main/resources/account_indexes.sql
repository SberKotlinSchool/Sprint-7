--liquibase formatted sql

--changeset dakorshik:add_account_id_index

create index account1_id_index on account1 (id);
