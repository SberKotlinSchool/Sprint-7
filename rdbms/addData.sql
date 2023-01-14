--liquibase formatted sql

--changeset aipopova:addData

INSERT INTO accounts (amount, version) VALUES (10, 0);
INSERT INTO accounts(amount, version) VALUES (8, 0);


