--liquibase formatted sql

--changeset msastashkin:index

CREATE INDEX account1_index_id ON account1 (id);