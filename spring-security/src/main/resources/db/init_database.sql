--liquibase formatted sql

--changeset dokl57:init

CREATE TABLE IF NOT EXISTS users
(
    id
    bigint
    NOT
    NULL,
    login
    varchar
(
    255
) NOT NULL,
    password varchar
(
    255
) NOT NULL,
    role varchar
(
    255
),
    CONSTRAINT users_pkey PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS role
(
    id
    bigint
    NOT
    NULL,
    role
    varchar
(
    255
),
    CONSTRAINT role_pkey PRIMARY KEY
(
    id
)
    );

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id
    bigint
    NOT
    NULL,
    roles_id
    bigint
    NOT
    NULL,
    FOREIGN
    KEY
(
    roles_id
)
    REFERENCES role
(
    id
) MATCH SIMPLE,
    FOREIGN KEY
(
    user_id
)
    REFERENCES users
(
    id
) MATCH SIMPLE
    );
