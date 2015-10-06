-- name: create-permission-table-if-not-exists!
-- creates the permission table if it does not exist
CREATE TABLE IF NOT EXISTS permissions (
    id          SERIAL PRIMARY KEY
  , permission TEXT UNIQUE NOT NULL);