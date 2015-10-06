-- name: create-password-reset-table-if-not-exists!
-- create the password_reset table if it does not exist
CREATE TABLE IF NOT EXISTS password_reset (
    id              SERIAL    PRIMARY KEY NOT NULL
  , reset_key     TEXT                  NOT NULL UNIQUE
  , already_used  BOOLEAN               NOT NULL DEFAULT FALSE
  , user_id       INTEGER   REFERENCES users (id) ON DELETE CASCADE
  , valid_until   TIMESTAMP WITH TIME ZONE DEFAULT CLOCK_TIMESTAMP() + INTERVAL '24 hours'
);