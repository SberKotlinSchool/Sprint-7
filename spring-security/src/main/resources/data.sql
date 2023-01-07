INSERT INTO USERS (CRED_EXPIRED, ENABLED, EXPIRED, LOCKED, NAME, PASSWORD, ROLES) VALUES
(false, true, false, false, 'admin', '$2a$12$awE/wfNNmhPgAnTKoNUgUeNYV5TEgBRQPwkqg35Mkg6UrUrFvsfpW', 'ROLE_ADMIN'),
(false, true, false, false, 'user', '$2a$12$D6yn3T80al/kDhsFkKtg9uJtKEry7yJKVCCQcGRJwJ6vldbO9Ys3u', 'ROLE_USER');

INSERT INTO NOTES (AUTHOR, CONTENT, POST_DATE) VALUES
('admin', 'First note by Admin', '"18.11 11:22:33"'),
('user', 'Second note by User', '"20.12 00:11:22"');