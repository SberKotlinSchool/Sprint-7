--liquibase formatted sql

--changeset idromanova:init_roles

CREATE TABLE IF NOT EXISTS roles
(
    id bigint NOT NULL,
    role varchar(255),
    CONSTRAINT role_pkey PRIMARY KEY (id)
    );

insert into roles (id, role) values
                                (1, 'ROLE_ADMIN'),
                                (2, 'ROLE_USER'),
                                (3, 'ROLE_API');