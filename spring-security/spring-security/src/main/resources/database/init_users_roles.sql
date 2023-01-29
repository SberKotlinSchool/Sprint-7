--liquibase formatted sql

--changeset idromanova:init_users_roles

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    FOREIGN KEY (role_id)  REFERENCES roles (id) ,
    FOREIGN KEY (user_id) REFERENCES users (id)
    );

insert into users_roles (user_id, role_id) values
                                                (1, 1),
                                                (1, 2),
                                                (2, 3);

