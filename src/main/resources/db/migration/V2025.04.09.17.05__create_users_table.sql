-- 1. Create the users table
CREATE TABLE users (
   user_id   SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL
);

-- 2. Insert test data
INSERT INTO users (name) VALUES
     ('Alice'),
     ('Bob'),
     ('Charlie');