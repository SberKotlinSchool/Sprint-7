CREATE TABLE account1
(
    id bigserial CONSTRAINT account_pk PRIMARY KEY,
    amount BIGINT CHECK (amount >= 0),
    version BIGINT
);

CREATE INDEX idx_account1_id ON account1 (id);

INSERT INTO accounts.account (id, amount, version)
VALUES (1, 1000, 0),
       (2, 2000, 0),
       (3, 5000, 0);

