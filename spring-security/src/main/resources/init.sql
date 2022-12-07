--liquibase formatted sql

--changeset aarozhok:init

CREATE TABLE IF NOT EXISTS Users (
	id BIGINT AUTO_INCREMENT,
	user_name VARCHAR NOT NULL UNIQUE,
	password VARCHAR NOT NULL,
	roles VARCHAR NOT NULL,
	is_locked BOOLEAN DEFAULT false,
	is_expired BOOLEAN DEFAULT false,
	is_cred_Expired BOOLEAN DEFAULT false,
	enabled BOOLEAN DEFAULT true,
	CONSTRAINT user_pk PRIMARY KEY (id)
);

INSERT INTO Users (`user_name`, `password`,`roles`, `is_locked`,  `is_expired`, `is_cred_Expired`, `enabled`)
VALUES
  ('admin', '$2a$10$ggt57oCIf8G5FQ55pd0U6O27J85IE6SA71AyaG4u9OJW5ta0yErvO', 'ROLE_ADMIN', false, false, false, true),
  ('user1', '$2a$10$F99xYjgdiVfG4YDYIZSz6uIPO.iU2fUyZKg/seuDusQ4LbUuT/Rfm', 'ROLE_USER', false, false, false, true),
  ('apiuser', '$2a$10$/AYG9qeHHT0h48k7RhUqk.LZxHwSYaAdwT4mGJ8vXOJKACbzON0J2', 'ROLE_USER_API', false, false, false, true);




