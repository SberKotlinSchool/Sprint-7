--liquibase formatted sql

--changeset dnbiryukov:accounts

CREATE TABLE accounts
(
    id bigserial,
    amount int CONSTRAINT positive_amount CHECK ( amount > 0 ),
    version int
);

--changeset dnbiryukov:accounts.id_pk

ALTER TABLE accounts ADD PRIMARY KEY (id);