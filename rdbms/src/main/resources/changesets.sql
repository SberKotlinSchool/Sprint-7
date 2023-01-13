--liquibase formatted sql

--changeset tdolgopolova:1
create table account1
(
    id bigserial constraint account_pk primary key,
    acc_number varchar(20),
    acc_name varchar(150),
    amount int CHECK (amount >= 0),
    version int
);
--rollback drop table account1;

--changeset tdolgopolova:2
insert into account1 (id, acc_number, acc_name, amount, version) values (1, "40701810000100001001", "счет 1", 500, 0);
insert into account1 (id, acc_number, acc_name, amount, version) values (2, "40701810000100001002", "счет 2", 200, 0);

--changeset tdolgopolova:3
create index account1_number_indx on account1 using hash(acc_number);






