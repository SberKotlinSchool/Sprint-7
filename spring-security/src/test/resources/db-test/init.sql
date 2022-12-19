--liquibase formatted sql

--changeset kulinichrs:init_users

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL,
    login varchar(255),
    password varchar(255),
    role varchar(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
    );

insert into users (id, login, password) values
                                            (1, 'admin', '$2y$10$PfQbnYEq9.1qtZ/wZmP6t.VVfajSkYsuxnCvoT0xySxQ9qV3oIFEW'),
                                            (2, 'gustav', '$2y$08$SLMe/h7yjt1NUCwPnXX7gOfbsP8Ey2wPz2F0xu6VIct8AkDpiRfGa');

--changeset kulinichrs:init_role

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

--changeset kulinichrs:init_user_role

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint NOT NULL,
    roles_id bigint NOT NULL,
     FOREIGN KEY (roles_id)  REFERENCES role (id) ,
    FOREIGN KEY (user_id) REFERENCES users (id)
    );

insert into users_roles (user_id, roles_id) values
                                                (1, 3),
                                                (2, 1),
                                                (2, 2);