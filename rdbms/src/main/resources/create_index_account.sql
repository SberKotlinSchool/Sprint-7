--liquibase formatted sql

--changeset sashamsheev:index

create index amount_idx ON account (id);