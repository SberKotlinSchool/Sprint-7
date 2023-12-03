DROP TABLE IF EXISTS user_data;
DROP TABLE IF EXISTS note;

create TABLE IF NOT EXISTS user_data
(
    id       bigint NOT NULL,
    login    varchar(255),
    password varchar(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

create TABLE IF NOT EXISTS note
(
    id      bigint NOT NULL,
    name    varchar(255),
    address varchar(255),
    phone   varchar(255),
    CONSTRAINT note PRIMARY KEY (id)
);

insert into user_data (id, login, password)
values (1, 'u', 'u')