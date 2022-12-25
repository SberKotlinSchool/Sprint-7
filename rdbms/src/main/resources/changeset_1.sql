--liquibase formatted sql

--changeset mstarikova:changeset_1

CREATE TABLE my_account
(
    id bigserial constraint my_account_pk primary key,
    amount int CHECK (amount >= 0),
    version int
);


INSERT INTO my_account (id, amount, version)
VALUES (1, 1000, 0),
       (2, 2000, 0);
