CREATE TABLE users.user_dietary_preferences
(
    user_id             VARCHAR(100)    NOT NULL,
    dietary_preference_id  uuid            NOT NULL,
    PRIMARY KEY (user_id, dietary_preference_id),
    FOREIGN KEY (user_id) REFERENCES users.users(id) ON DELETE CASCADE,
    FOREIGN KEY (dietary_preference_id) REFERENCES users.dietary_preferences(id) ON DELETE CASCADE
)