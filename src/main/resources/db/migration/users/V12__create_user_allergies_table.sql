CREATE TABLE users.user_allergies
(
    user_id    VARCHAR(100) NOT NULL,
    allergy_id UUID         NOT NULL,
    PRIMARY KEY (user_id, allergy_id),
    FOREIGN KEY (user_id) REFERENCES users.users(id) ON DELETE CASCADE,
    FOREIGN KEY (allergy_id) REFERENCES users.allergies(id) ON DELETE CASCADE
);