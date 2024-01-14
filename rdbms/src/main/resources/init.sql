create table accounts
(
    id bigserial
        constraint account_pk primary key,
    amount  int,
    version int
);

insert into accounts
values (1, 100, 1),
       (2, 1200, 1);


