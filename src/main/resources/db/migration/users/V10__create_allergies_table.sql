CREATE TABLE users.allergies
(
    id          UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    is_active   BOOLEAN      NOT NULL,
    CONSTRAINT pk_allergies PRIMARY KEY (id)
);