DROP TABLE IF EXISTS user_data_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS user_data;
DROP TABLE IF EXISTS note;

create TABLE IF NOT EXISTS user_data
(
    id       bigint NOT NULL,
    login    varchar(255),
    password varchar(255),
    roles    varchar(255),
    CONSTRAINT user_data_pkey PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS note
(
    id      bigint NOT NULL,
    name    varchar(255),
    address varchar(255),
    phone   varchar(255),
    CONSTRAINT note PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS roles
(
    id    bigint NOT NULL,
    roles varchar(255),
    CONSTRAINT role_pkey PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS user_data_roles
(
    user_id  bigint NOT NULL,
    roles_id bigint NOT NULL,
    CONSTRAINT C_USER_ROLE FOREIGN KEY (roles_id)
        REFERENCES roles (id),
    CONSTRAINT C_ROLE_USER FOREIGN KEY (user_id)
        REFERENCES user_data (id)
);

insert into user_data (id, login, password)
values (1, 'api', '$2a$10$6k8ogL41MOwrSnpvMQ9C9.lzu614qsDvMjPKb0H85vGnM/rSJwN76'),
       (2, 'admin', '$2a$10$HAOqnZ3W3YHXjik2OxvyGOWVIP5IAfFAibm8JT3FhQrhyPM8puS1e'),
       (3, 'user', '$2a$10$zF5bIwkaf2IAfMjqMqAHPOdnRpykD2dCeipBzP2Oq1GbdLaDJsq7e');

insert into roles (id, roles)
values (1, 'ADMIN'),
       (2, 'USER'),
       (3, 'API');

insert into user_data_roles (user_id, roles_id)
values (1, 3),
       (2, 1),
       (2, 2),
       (3, 2);
