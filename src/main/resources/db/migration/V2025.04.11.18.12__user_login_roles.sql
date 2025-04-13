-- 1. Create the roles table
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    authority VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Add username and password columns to the users table
ALTER TABLE users
    ADD COLUMN username VARCHAR(100) UNIQUE,
    ADD COLUMN password VARCHAR(255);

-- 3. Create join table for many-to-many relation between users and roles
CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE
);

-- 4. Insert roles
INSERT INTO roles (authority) VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');

-- 5. Update test users
UPDATE users SET username = 'alice' WHERE name = 'Alice';
UPDATE users SET username = 'bob' WHERE name = 'Bob';
UPDATE users SET username = 'charlie' WHERE name = 'Charlie';

-- 6. Assign roles to users (example: all as ROLE_USER)
INSERT INTO user_role (user_id, role_id)
SELECT user_id, (SELECT role_id FROM roles WHERE authority = 'ROLE_USER')
FROM users;