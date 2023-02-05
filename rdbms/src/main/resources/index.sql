--liquibase formatted sql

--changeset vorotovav:index

create index account1_id_idx on account1 using (id);