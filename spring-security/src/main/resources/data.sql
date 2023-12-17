DROP TABLE IF EXISTS note;

create TABLE IF NOT EXISTS note
(
    id      bigint NOT NULL,
    name    varchar(255),
    address varchar(255),
    phone   varchar(255),
    CONSTRAINT note PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users cascade;
DROP index IF EXISTS ix_auth_username;
create table IF NOT EXISTS users
(
    username varchar_ignorecase(50)  not null primary key,
    password varchar_ignorecase(500) not null,
    enabled  boolean                 not null
);

create table IF NOT EXISTS authorities
(
    username  varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index IF NOT EXISTS ix_auth_username on authorities (username, authority);