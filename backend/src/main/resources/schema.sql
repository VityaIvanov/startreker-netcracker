DROP TABLE assigned_role;
DROP TABLE usr;
DROP TABLE role;

CREATE TABLE usr(
                  user_id BIGSERIAL PRIMARY KEY,
                  user_name VARCHAR(64) not null,
                  user_password VARCHAR(128) not null,
                  user_email VARCHAR(128) not null,
                  user_telephone VARCHAR(128) not null,
                  user_token VARCHAR(256),
                  user_activated BOOLEAN,
                  user_created DATE
);

CREATE TABLE role(
                   role_id BIGSERIAL PRIMARY KEY,
                   role_name VARCHAR(64) not null
);

CREATE TABLE assigned_role(
                            user_id BIGSERIAL REFERENCES usr (user_id),
                            role_id BIGSERIAL REFERENCES role (role_id)
);

INSERT INTO role (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO role (role_name) VALUES ('ROLE_USER');
INSERT INTO role (role_name) VALUES ('ROLE_CARRIER');