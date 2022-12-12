create table accounts (
    id int primary key,
    amount int default 0 check (amount >= 0),
    version int default 0
);

create unique index idx_accounts on accounts(id, version); -- по заданию нужен второй индекс

