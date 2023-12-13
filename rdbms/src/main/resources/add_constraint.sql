--liquibase formatted sql

--changeset iedenisov:add_content

ALTER TABLE account1 ADD CONSTRAINT account1_chk_positive_amount CHECK (amount >= 0)