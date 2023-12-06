create table account
(
    id         bigserial constraint account_pk PRIMARY KEY,
    amount     int       NOT NULL default 0 CHECK (amount >= 0),
    version    int NOT NULL default 1
);
