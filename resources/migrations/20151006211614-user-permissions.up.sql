-- name: create-user-permission-table-if-not-exists!
-- create the user_permission table if it does not exist
CREATE TABLE IF NOT EXISTS user_permission (
    id           SERIAL  PRIMARY KEY
  , user_id    INTEGER REFERENCES users (id)    ON DELETE CASCADE
  , permission TEXT    REFERENCES permissions (permission) ON DELETE CASCADE);

