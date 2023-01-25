--liquibase formatted sql

--changeset addressbook:init_users

--drop table users cascade ;
--drop table roles cascade;
--drop table users_roles cascade;

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL,
    login varchar(255),
    password varchar(255),
    roles varchar(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

insert into users (id, login, password) values
                                            (1, 'api', '$2a$10$9jpvrmtC.Gw1b.MPYAsxleZ33/7OiHgGhGuO8890iQDJJxZ3t7Bsi'),
                                            (2, 'admin', '$2a$10$KXbFaQ64Y.xKs5/QtsDF1.tXpZx2z7Y58apqrqjbXcr7TAeQRhLJW'),
                                            (3, 'pavel', '$2a$10$xzXU.cNE/y5oGBjDOY//ce38BbE8OdAdywNHXXqv.5fqLtIHYycEK');

--changeset addressbook:init_roles

CREATE TABLE IF NOT EXISTS roles
(
    id bigint NOT NULL,
    roles varchar(255),
    CONSTRAINT role_pkey PRIMARY KEY (id)
);

insert into roles (id, roles) values
                                (1, 'ROLE_ADMIN'),
                                (2, 'ROLE_USER'),
                                (3, 'ROLE_API');

--changeset addressbook:init_user_role

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint NOT NULL,
    roles_id bigint NOT NULL,
    CONSTRAINT C_USER_ROLE FOREIGN KEY (roles_id)
        REFERENCES roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT C_ROLE_USER FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

insert into users_roles (user_id, roles_id) values
                                                (1, 3),
                                                (2, 1),
                                                (2, 2),
                                                (3, 2);