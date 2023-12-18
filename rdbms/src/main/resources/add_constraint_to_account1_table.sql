--liquibase formatted sql

--changeset dokl57:add_constraint_to_account1_table

ALTER TABLE account1 ADD CONSTRAINT account1_chk_positive_amount CHECK (amount >= 0)
