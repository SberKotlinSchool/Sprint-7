--liquibase formatted sql

--changeset valentina:addConstraint
alter table account1 add constraint check_amount check (amount >= 0);