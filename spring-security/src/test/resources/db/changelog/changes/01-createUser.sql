create table sec.roles (
    user_id uuid not null,
    role varchar(255) not null,
    primary key (user_id, role)
);

create table sec.sec_user (
    id uuid not null,
    created_date timestamp not null,
     password_hash varchar(255) not null,
     is_non_locked boolean not null,
     login varchar(255) not null,
     name varchar(255) not null, primary key (id)
);

alter table if exists sec.sec_user
    add constraint UK_lptq0wmhi2xwpe07kfgjy5ale unique (login);

alter table if exists sec.roles
    add constraint FKmp1n833pia625w2s6a06abn03 foreign key (user_id) references sec.sec_user;
