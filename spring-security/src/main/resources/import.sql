INSERT INTO USER (`username`, `password`,`groups`, `cred_expired`,  `enabled`, `expired`, `locked`) values ('admin','{noop}admin', 'ROLE_ADMIN', false,true,false,false ),('test','{noop}test','ROLE_API', false,true,false,false  ),('test1','{noop}test1', 'ROLE_USER',false,true,false,false  );
INSERT INTO AUTHOR (`first_name`, `second_name`, `owner`) VALUES ('Test1', 'Testov1', 0), ('Test11', 'Testov11', 0), ('Test12', 'Testov12', 0);
INSERT INTO AUTHOR (`first_name`, `second_name`, `owner`) VALUES ('Test2', 'Testov2', 1), ('Test21', 'Testov21', 1), ('Test22', 'Testov22', 1);
INSERT INTO AUTHOR (`first_name`, `second_name`, `owner`) VALUES ('Test3', 'Testov3', 2), ('Test31', 'Testov31', 2), ('Test32', 'Testov32', 2);
INSERT INTO AUTHOR (`first_name`, `second_name`, `owner`) VALUES ('Test4', 'Testov4', 0), ('Test41', 'Testov41', 0), ('Test42', 'Testov42', 0);
INSERT INTO ACL_CLASS (`id`, `class`) VALUES (1, 'com.example.demo.persistance.Author');
INSERT INTO ACL_SID(`id`, `sid`, `principal`) VALUES (1, 'admin', 1), (2, 'user1', 1), (3, 'user2', 1),  (4, 'ROLE_ADMIN', 0), (5, 'ROLE_API', 0), (6, 'ROLE_USER', 0);
INSERT INTO ACL_OBJECT_IDENTITY(`id`, `object_id_class`, `object_id_identity`, `owner_sid`, `entries_inheriting`) VALUES (1, 1, 1, 5, 0), (2, 1, 2, 5, 0), (3, 1, 3, 5, 0), (4, 1, 4, 1, 0), (5, 1, 5, 1, 0), (6, 1, 6, 1, 0), (7, 1, 7, 2, 0), (8, 1, 8, 2, 0), (9, 1, 9, 2, 0), (10, 1, 10, 3, 0), (11, 1, 11, 3, 0), (12, 1, 12, 3, 0);
INSERT INTO ACL_ENTRY(`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`, `audit_success`, `audit_failure`) VALUES (1, 1, 5, 1, 1, 1, 1), (2, 1, 5, 1, 1, 1, 1), (3, 1, 1, 8, 1, 1, 1), (4, 1, 1, 8, 1, 1, 1), (5, 1, 1, 8, 1, 1, 1), (6, 1, 1, 8, 1, 1, 1), (7, 1, 2, 8, 1, 1, 1), (8, 1, 2, 8, 1, 1, 1), (9, 1, 2, 8, 1, 1, 1), (10, 1, 3, 8, 1, 1, 1), (11, 1, 3, 8, 1, 1, 1), (12, 1, 3, 8, 1, 1, 1);

