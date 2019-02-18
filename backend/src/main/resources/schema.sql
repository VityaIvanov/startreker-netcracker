CREATE TABLE usr
(
  user_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_name          VARCHAR(64)  not null,
  user_password      VARCHAR(128) not null,
  user_email         VARCHAR(128) not null,
  user_token VARCHAR(256),
  user_telephone VARCHAR(256),
  user_activated BOOLEAN,
  user_created DATE
);

CREATE TABLE role
(
  role_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_name VARCHAR(64) not null
);

CREATE TABLE assigned_role
(
  user_id BIGINT REFERENCES usr (user_id),
  role_id BIGINT REFERENCES role (role_id)
);

INSERT INTO role (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO role (role_name) VALUES ('ROLE_USER');
INSERT INTO role (role_name) VALUES ('ROLE_CARRIER');

INSERT INTO usr (user_id, user_name, user_password, user_email, user_telephone, user_token, user_activated, user_created)
VALUES (1, 'vitya', 'asdsad','a1','asdsad', 'asdsad', false, NOW());
INSERT INTO usr (user_id, user_name, user_password, user_email, user_telephone, user_token, user_activated, user_created)
VALUES (2, 'a', 'asdsad','a2','asdsad', 'asdsad', false,  NOW());
INSERT INTO usr (user_id, user_name, user_password, user_email, user_telephone, user_token, user_activated, user_created)
VALUES (3, 'b', 'asdsad','a3','asdsad', 'asdsad', false,  NOW());
INSERT INTO usr (user_id, user_name, user_password, user_email, user_telephone, user_token, user_activated, user_created)
VALUES (4, 'c', 'asdsad','a4','asdsad', 'asdsad', false,  NOW());
INSERT INTO usr (user_id, user_name, user_password, user_email, user_telephone, user_token, user_activated, user_created)
VALUES (5, 'd', 'asdsad','a5','asdsad', 'asdsad', false,  NOW());
INSERT INTO usr (user_id, user_name, user_password, user_email, user_telephone, user_token, user_activated, user_created)
VALUES (6, 'e', 'asdsad','a6','asdsad', 'asdsad', false,  NOW());
INSERT INTO usr (user_id, user_name, user_password, user_email, user_telephone, user_token, user_activated, user_created)
VALUES (7, 'f', 'asdsad','a7','asdsad', 'asdsad', false,  NOW());


INSERT INTO assigned_role (user_id, role_id) VALUES (1, 3);
INSERT INTO assigned_role (user_id, role_id) VALUES (1, 2);

INSERT INTO assigned_role (user_id, role_id) VALUES (2, 3);
INSERT INTO assigned_role (user_id, role_id) VALUES (2, 2);

INSERT INTO assigned_role (user_id, role_id) VALUES (3, 1);
INSERT INTO assigned_role (user_id, role_id) VALUES (3, 2);

INSERT INTO assigned_role (user_id, role_id) VALUES (4, 2);
INSERT INTO assigned_role (user_id, role_id) VALUES (5, 2);

INSERT INTO assigned_role (user_id, role_id) VALUES (6, 1);
INSERT INTO assigned_role (user_id, role_id) VALUES (6, 3);

INSERT INTO assigned_role (user_id, role_id) VALUES (7, 3);