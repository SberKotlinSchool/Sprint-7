--liquibase formatted sql

--changeset synkovav:init

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL,
    login varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

insert into users (id, login, password) values
    (1, 'user1', '$2a$12$EDXtLIyJa8SK6VZy3UEshuY.z/5x5cqagbbAjmdcUYRx6NMjbBNvq'),
    (2, 'user2', '$2a$12$s6pSn7y98sAZ.55MLlJGseTYxYXxD.JifgLjZhbMJe1v1U50kpRuS');

CREATE TABLE IF NOT EXISTS role
(
    id bigint NOT NULL,
    role varchar(255),
    CONSTRAINT role_pkey PRIMARY KEY (id)
    );

insert into role (id, role) values
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_API');

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint NOT NULL,
    roles_id bigint NOT NULL,
    FOREIGN KEY (roles_id)
    REFERENCES role (id) MATCH SIMPLE,
    FOREIGN KEY (user_id)
    REFERENCES users (id) MATCH SIMPLE
    );

insert into users_roles (user_id, roles_id) values
    (1, 3),
    (2, 1),
    (2, 2);