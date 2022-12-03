--liquibase formatted sql

--changeset synkovav:init

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL,
    login varchar(255),
    password varchar(255),
    role varchar(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

insert into users (id, login, password) values
    (1, 'ivan', '$2y$10$PfQbnYEq9.1qtZ/wZmP6t.VVfajSkYsuxnCvoT0xySxQ9qV3oIFEW'),
    (2, 'inna', '$2y$08$SLMe/h7yjt1NUCwPnXX7gOfbsP8Ey2wPz2F0xu6VIct8AkDpiRfGa');

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
    CONSTRAINT fk4653ut8m260oyset67f4o5dfs FOREIGN KEY (roles_id)
    REFERENCES role (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fkeqn66nas8rv26sso83h5k3n69 FOREIGN KEY (user_id)
    REFERENCES users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

insert into users_roles (user_id, roles_id) values
    (1, 3),
    (2, 1),
    (2, 2);