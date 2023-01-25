--changeSet astafex:init
CREATE TABLE account
(
    id      BIGSERIAL
        CONSTRAINT account_pk PRIMARY KEY,
    amount  BIGINT CHECK ( amount > 0 ),
    version BIGINT
);

--changeSet astafex:add_index
CREATE INDEX account1_id_index ON account (id);

--changeSet astafex:add_accounts_data
INSERT INTO account VALUES (1,10000,0);
INSERT INTO account VALUES (2,20000,0);
