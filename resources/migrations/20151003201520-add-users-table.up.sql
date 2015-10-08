-- name: create-user-table-if-not-exists!
-- create the user table if it does not exist

CREATE TABLE IF NOT EXISTS users(
 id            SERIAL PRIMARY KEY NOT NULL,
 username      VARCHAR(100) NOT NULL,
 email         VARCHAR(100) NOT NULL UNIQUE,
 password      TEXT,
 refresh_token TEXT
);
