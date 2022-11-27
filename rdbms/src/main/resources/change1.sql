-- liquibase formatted sql

-- changeset aarozhok:1

CREATE TABLE account2
(
    id bigserial CONSTRAINT account_pk primary key,
    amount NUMERIC CHECK (amount >= 0),
    version INT
);

INSERT INTO public.account2 (amount, version) VALUES (1000, 0);
INSERT INTO public.account2 (amount, version) VALUES (500, 0);

