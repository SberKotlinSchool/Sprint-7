--liquibase formatted sql

-- Создает индекс для наиболее эффективного доступа к счетам

CREATE INDEX amount_index ON accounts (amount);