truncate sec.roles cascade;
truncate sec.sec_user cascade ;

insert into sec.sec_user (id, created_date, password_hash, is_non_locked, login, name)
    values ('9cb2f4dc-ba70-4fae-bd2a-547de191219a', '2023-01-19 01:09:36.229207', 'hash', true, 'user', 'user'),
           ('9cb2f4dc-ba70-4fae-bd2a-547de191219b', '2023-01-19 01:09:36.229208', 'hash', true, 'deleter', 'deleter'),
           ('9cb2f4dc-ba70-4fae-bd2a-547de191219c', '2023-01-19 01:09:36.229209', 'hash', true, 'admin', 'admin');

insert into sec.roles(user_id, role)
    values ('9cb2f4dc-ba70-4fae-bd2a-547de191219a', 'USER'),
           ('9cb2f4dc-ba70-4fae-bd2a-547de191219b', 'DELETER'),
           ('9cb2f4dc-ba70-4fae-bd2a-547de191219c', 'ADMIN');