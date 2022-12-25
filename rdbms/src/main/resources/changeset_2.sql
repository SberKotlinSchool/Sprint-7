--liquibase formatted sql

--changeset mstarikova:changeset_2

CREATE INDEX my_account_version_id_idx ON my_account ("version",id);