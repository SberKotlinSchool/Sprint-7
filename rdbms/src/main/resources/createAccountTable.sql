create table accounts.account
(
    id bigserial constraint account_pk primary key,
    amount int check (amount >= 0),
    version int
);