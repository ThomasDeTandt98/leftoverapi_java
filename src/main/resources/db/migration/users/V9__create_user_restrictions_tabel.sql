CREATE TABLE users.user_restrictions
(
    user_id        VARCHAR(100) NOT NULL,
    restriction_id UUID         NOT NULL,
    PRIMARY KEY (user_id, restriction_id),
    FOREIGN KEY (user_id) REFERENCES users.users(id) ON DELETE CASCADE,
    FOREIGN KEY (restriction_id) REFERENCES users.restrictions(id) ON DELETE CASCADE
);