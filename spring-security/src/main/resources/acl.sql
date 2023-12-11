CREATE TABLE IF NOT EXISTS acl_sid (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    principal tinyint(1) NOT NULL,
    sid varchar(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uk_1 (sid,principal)
    );

CREATE TABLE IF NOT EXISTS acl_class (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    class varchar(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uk_2 (class)
    );

CREATE TABLE IF NOT EXISTS acl_object_identity (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    object_id_class bigint(20) NOT NULL,
    object_id_identity bigint(20) NOT NULL,
    parent_object bigint(20) DEFAULT NULL,
    owner_sid bigint(20) DEFAULT NULL,
    entries_inheriting tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uk_3 (object_id_class,object_id_identity)
    );
ALTER TABLE acl_object_identity ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);
ALTER TABLE acl_object_identity ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);
ALTER TABLE acl_object_identity ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);

CREATE TABLE IF NOT EXISTS acl_entry (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    acl_object_identity bigint(20) NOT NULL,
    ace_order int(11) NOT NULL,
    sid bigint(20) NOT NULL,
    mask int(11) NOT NULL,
    granting tinyint(1) NOT NULL,
    audit_success tinyint(1) NOT NULL,
    audit_failure tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY unique_uk_4 (acl_object_identity,ace_order)
    );
ALTER TABLE acl_entry ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);
ALTER TABLE acl_entry ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

-- ACL
INSERT INTO acl_sid (id, principal, sid) VALUES
                                             (1, 1, 'supe'),
                                             (2, 1, 'user2'),
                                             (3, 1, 'admin'),
                                             (4, 1, 'adminapi');

INSERT INTO acl_class (id, class) VALUES (1, 'ru.shadowsith.addressbook.dto.Record');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
                                                                                                                            (1, 1, -1, NULL, 3, 0),
                                                                                                                            (2, 1, -2, NULL, 3, 0),
                                                                                                                            (3, 1, -3, NULL, 3, 0),
                                                                                                                            (4, 1, -4, NULL, 3, 0);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
                                                                                                                  (1, 1, -1, 1, 1, 1, 1, 1),
                                                                                                                  (2, 1, -2, 2, 1, 1, 1, 1),
                                                                                                                  (3, 1, -3, 3, 1, 1, 1, 1),
                                                                                                                  (4, 1, -4, 4, 1, 1, 1, 1),
                                                                                                                  (5, 2, -1, 1, 1, 1, 1, 1),
                                                                                                                  (6, 2, -2, 2, 1, 1, 1, 1),
                                                                                                                  (7, 2, -3, 3, 1, 1, 1, 1),
                                                                                                                  (8, 2, -4, 4, 1, 1, 1, 1),
                                                                                                                  (9, 3, -1, 1, 1, 1, 1, 1),
                                                                                                                  (10, 3, -2, 2, 1, 1, 1, 1),
                                                                                                                  (11, 3, -3, 3, 1, 1, 1, 1),
                                                                                                                  (12, 3, -4, 4, 1, 1, 1, 1),
                                                                                                                  (13, 4, -1, 1, 1, 1, 1, 1),
                                                                                                                  (14, 4, -2, 2, 1, 1, 1, 1),
                                                                                                                  (15, 4, -3, 3, 1, 1, 1, 1),
                                                                                                                  (16, 4, -4, 4, 1, 1, 1, 1);
INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
                                                                                                                  (5, 1, -1, 1, 3, 1, 1, 1),
                                                                                                                  (6, 2, -2, 1, 3, 1, 1, 1),
                                                                                                                  (7, 3, -3, 1, 3, 1, 1, 1),
                                                                                                                  (8, 4, -4, 1, 3, 1, 1, 1);