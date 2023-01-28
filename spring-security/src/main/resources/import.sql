INSERT INTO USER (`username`, `password`,`groups`, `cred_expired`,  `enabled`, `expired`, `locked`) VALUES ('admin', '{bcrypt}$2a$10$/Dk18kq08XhdvkYoOtqzlOkFGVEbazrKqnbZ/WpmPhlPIhNF2wHMK', 'ROLE_ADMIN', false, true, false, false), ('user1', '{bcrypt}$2a$10$RyKnfbXfbpxv9xyrS4Fi4ujJ52BZ6P9exoogH4V8rc/3pL/3x7wO.', 'ROLE_USER', false, true, false, false), ('user2', '{bcrypt}$2a$10$T050JBW7EgDZuVB2X1pA2eXjNhzGYJlDV0jCBWWHWX1AaHmGlIgT2', 'ROLE_USER', false, true, false, false);
INSERT INTO ADDRESSBOOK (`firstName`, `lastName`, `city`, `owner`) VALUES ('Ivan','Ivanov_admin','Ivanovo', 0), ('Dmitry','Dmitriev_admin','Dmitrov', 0), ('Andrey','Andreev_admin','Andreevka', 0);
INSERT INTO ADDRESSBOOK (`firstName`, `lastName`, `city`, `owner`) VALUES ('Ivan1','Ivanov_user1','Ivanovo1', 1), ('Dmitry1','Dmitriev_user1','Dmitrov1', 1), ('Andrey1','Andreev_user1','Andreevka1', 1);
INSERT INTO ADDRESSBOOK (`firstName`, `lastName`, `city`, `owner`) VALUES ('Ivan2','Ivanov_user2','Ivanovo2', 2), ('Dmitry2','Dmitriev_user2','Dmitrov2', 2), ('Andrey2','Andreev_user2','Andreevka2', 2);
INSERT INTO acl_class (id, class) VALUES (1, 'ru.sber.app.entity.AddressBook');
INSERT INTO ACL_SID(`id`, `sid`, `principal`) VALUES (1, 'admin', 1), (2, 'user1', 1), (3, 'user2', 1),  (4, 'ROLE_ADMIN', 0), (5, 'ROLE_USER', 0);
INSERT INTO ACL_OBJECT_IDENTITY(`id`, `object_id_class`, `object_id_identity`, `owner_sid`, `entries_inheriting`) VALUES (1, 1, 1, 5, 0), (2, 1, 2, 5, 0), (3, 1, 3, 5, 0), (4, 1, 4, 1, 0), (5, 1, 5, 1, 0), (6, 1, 6, 1, 0), (7, 1, 7, 2, 0), (8, 1, 8, 2, 0), (9, 1, 9, 2, 0);
INSERT INTO ACL_ENTRY(`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`, `audit_success`, `audit_failure`) VALUES (1, 1, 5, 1, 1, 1, 1), (2, 1, 5, 1, 1, 1, 1), (3, 1, 1, 8, 1, 1, 1), (4, 1, 1, 8, 1, 1, 1), (5, 1, 1, 8, 1, 1, 1), (6, 1, 1, 8, 1, 1, 1), (7, 1, 2, 8, 1, 1, 1), (8, 1, 2, 8, 1, 1, 1), (9, 1, 2, 8, 1, 1, 1);