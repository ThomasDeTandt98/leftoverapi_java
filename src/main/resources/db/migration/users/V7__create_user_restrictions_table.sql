CREATE TABLE users.restrictions
(
    id          UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    is_active   BOOLEAN      NOT NULL,
    CONSTRAINT pk_restrictions PRIMARY KEY (id)
)