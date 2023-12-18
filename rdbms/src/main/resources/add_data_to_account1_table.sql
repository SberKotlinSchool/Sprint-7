--liquibase formatted sql

--changeset dokl57:add_data_to_account1_table

INSERT INTO account1 VALUES (1, 100, 0), (2, 200, 0)