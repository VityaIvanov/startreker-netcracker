DROP TABLE IF EXISTS assigned_role;
DROP TABLE IF EXISTS USER_A;
DROP TABLE IF EXISTS ROLE_A;

CREATE TABLE USER_A
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

CREATE TABLE ROLE_A
(
  role_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_name VARCHAR(64) not null
);

CREATE TABLE assigned_role
(
  user_id BIGINT REFERENCES USER_A (user_id),
  role_id BIGINT REFERENCES ROLE_A (role_id)
);

INSERT INTO ROLE_A (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE_A (role_name) VALUES ('ROLE_USER');
INSERT INTO ROLE_A (role_name) VALUES ('ROLE_CARRIER');