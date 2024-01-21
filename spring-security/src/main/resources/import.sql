-- FIXME есть ли возможность писать запросы в import.sql не в одну строку?
INSERT INTO user(username, password, groups, cred_expired, enabled, expired, locked) VALUES ('admin', '{bcrypt}$2a$10$onwi0RnDr9nZXdLb2QBzeed1S4Kxs1217Kcz3iLz.2Yiwbt5fVX56', 'ROLE_ADMIN', false, true, false, false), ('user', '{bcrypt}$2a$10$Jl6CAaqRnuQlPKLmOvMKy.SmBZIGC447l14KjKiENSfvkEx0bDqmC', 'ROLE_USER', false, true, false, false), ('user_api', '{bcrypt}$2a$10$xTyKbfkGcnajya9pXFs43.82ghMLRArLn3zZgThnPKjDbMhTMJ6au', 'ROLE_API', false, true, false, false), ('user_api_delete', '{bcrypt}$2a$10$jBkzg68v4PidV562RegdKOEoBBdzqyt4SJpBiMNyU5tL7HfKarzHC', 'ROLE_API_DELETE', false, true, false, false);
INSERT INTO acl_sid(id, sid, principal) VALUES (1, 'admin', 1), (2, 'user', 1), (3, 'user_api', 1), (4, 'user_api_delete', 1), (5, 'ROLE_USER', 0);
INSERT INTO acl_class(id, class) VALUES (1, 'com.firebat.addressbook.model.Entry');
INSERT INTO acl_object_identity(id, object_id_class, object_id_identity, owner_sid, entries_inheriting) VALUES (1, 1, 1, 1, 0), (2, 1, 2, 2, 0), (3, 1, 3, 3, 0), (4, 1, 4, 4, 0);
INSERT INTO acl_entry(acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (1, 1, 1, 8, 1, 1, 1), (2, 1, 2, 8, 1, 1, 1), (3, 1, 3, 8, 1, 1, 1), (4, 1, 4, 8, 1, 1, 1);