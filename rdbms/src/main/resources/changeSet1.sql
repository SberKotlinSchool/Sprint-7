--liquibase formatted sql

-- Создает необходимую таблицу со счетами

CREATE TABLE accounts {

    ID      bigserial CONSTRAINT account_pk PRIMARY KEY,
    AMOUNT  INT CHECK(AMOUNT > 0),
    VERSION INT
}

INSERT INTO accounts (id, amount, version)
            VALUES (1, 5000, 0),
                   (2, 7000, 0);