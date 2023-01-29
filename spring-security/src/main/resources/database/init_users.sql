--liquibase formatted sql

--changeset idromanova:init_users

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL,
    login varchar(255),
    password varchar(255),
    CONSTRAINT users_pk PRIMARY KEY (id)
);

insert into users (id, login, password) values
                                            (1, 'admin', '$2y$10$1zg7RIVFuGOHfj4y26KK5O2oMmfTledvG5qnLNtEOFLQQfKDfprma'),
                                            (2, 'test_user', '$2y$10$sMtNVJNMdMUGdXvz1ovfDeIEpoHfXpQ9KZ6Rr5PxC7MfziD00nrrm');
