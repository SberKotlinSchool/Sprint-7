--liquibase formatted sql

--changeset iedenisov:add_content

INSERT INTO account1 (id, amount, version)
VALUES (1, 100, 0),
       (2, 200, 0)