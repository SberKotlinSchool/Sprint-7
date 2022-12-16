--liquibase formatted sql

--changeset aimshenik:init

drop table if exists authorities;
drop table if exists users;

create table users
(
    id       bigint not null,
    username varchar(50) not null,
    password varchar(150),
    enabled  boolean     not null
);

create table authorities
(
    id        bigint not null,
    username  varchar(50) not null,
    authority varchar(50),
    constraint fk_authorities_user foreign key (username) references users (username)
);

insert into users(id, username, password, enabled)
values (1, 'user', '$2a$10$efTy6tMg2XaJWUquXAofzOvdUQUOcIP3kZE5KT16XqBNMEModOcLK', 1),
       (2, 'admin', '$2a$10$efTy6tMg2XaJWUquXAofzOvdUQUOcIP3kZE5KT16XqBNMEModOcLK', 1),
       (3, 'apiuser', '$2a$10$efTy6tMg2XaJWUquXAofzOvdUQUOcIP3kZE5KT16XqBNMEModOcLK', 1);

insert into authorities (id, username, authority)
values (1, 'user', 'USER'),
       (2, 'admin', 'ADMIN'),
       (3, 'apiuser', 'APIUSER');