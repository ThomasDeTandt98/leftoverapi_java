ALTER TABLE users.users
    DROP COLUMN firstName,
    DROP COLUMN lastName,
    ADD COLUMN first_name VARCHAR(255) NULL,
    ADD COLUMN last_name VARCHAR(255) NULL;