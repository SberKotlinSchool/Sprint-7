INSERT INTO ROLES (id, name) VALUES (0, 'ADMIN'), (1, 'API'), (2, 'OTHER');
INSERT INTO USERS (id, login, password) VALUES (0, 'admin', '$2a$12$n9RfnQ7cUSc9TNeqhuz5vOjrYBj29J4LZF0jVdH2qsIkTyYX2TID2'), (1, 'user1', '$2a$12$n9RfnQ7cUSc9TNeqhuz5vOjrYBj29J4LZF0jVdH2qsIkTyYX2TID2'), (2, 'user2', '$2a$12$n9RfnQ7cUSc9TNeqhuz5vOjrYBj29J4LZF0jVdH2qsIkTyYX2TID2');
INSERT INTO USERS_ROLES (user_id, roles_id) VALUES (0, 0), (1,1), (2,2);
INSERT INTO PERSONS (name, city) VALUES ('Alex', 'City1'), ('Boris', 'City2'), ('Colin', 'City3');
