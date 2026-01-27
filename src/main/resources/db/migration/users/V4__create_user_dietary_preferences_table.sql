CREATE TABLE users.dietary_preferences
(
    id        UUID         NOT NULL,
    name      VARCHAR(255) NOT NULL,
    is_active BOOLEAN      NOT NULL,
    CONSTRAINT pk_dietary_preferences PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);