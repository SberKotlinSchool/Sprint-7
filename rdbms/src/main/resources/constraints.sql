--liquibase formatted sql

--changeset msastashkin:constraint

ALTER TABLE account1 ADD CONSTRAINT account1_positive_ammount CHECK (amount >= 0)