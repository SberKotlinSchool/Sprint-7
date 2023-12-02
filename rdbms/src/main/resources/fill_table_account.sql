--liquibase formatted sql

--changeset sashamsheev:fill

insert into account (id, amount, version)
values (1, 1000, 0),
       (2, 2000, 0),
       (3, 3000, 0),
       (4, 0, 0),
       (5, 0, 0);
