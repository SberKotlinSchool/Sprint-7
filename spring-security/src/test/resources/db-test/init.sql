--liquibase formatted sql

--changeset valentina:init_users

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL,
    nickname varchar(255),
    password varchar(255),
    role varchar(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

insert into users (id, nickname, password) values
    (1, 'ivan', '$2y$10$PfQbnYEq9.1qtZ/wZmP6t.VVfajSkYsuxnCvoT0xySxQ9qV3oIFEW'),
    (2, 'inna', '$2y$08$SLMe/h7yjt1NUCwPnXX7gOfbsP8Ey2wPz2F0xu6VIct8AkDpiRfGa');

--changeset valentina:init_role

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

--changeset valentina:init_user_role

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint NOT NULL,
    roles_id bigint NOT NULL,
    foreign key (user_id) references users(id),
    foreign key (roles_id) references role(id)
);

insert into users_roles (user_id, roles_id) values
    (1, 3),
    (2, 1),
    (2, 2);