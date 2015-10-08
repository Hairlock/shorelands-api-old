-- name: get-users
-- Retrieves all users from the database
SELECT * FROM users

-- name: get-user-by-id
-- Retrieves the user matching the supplied id
SELECT * FROM users
WHERE id = :id


-- name: get-user-by-username
-- Retrieves the user matching the supplied username
SELECT * FROM users
WHERE username = :username

-- name: get-user-by-email
-- Retrieves the user matching the supplied email address
SELECT * FROM users
WHERE email = :email

-- name: get-user-details-by-email
-- Selects user details for matching username
SELECT    user.id,
  user.email,
  user.username,
  user.password,
  user.refresh_token,
  STRING_AGG(perm.permission, ',')  AS permissions
FROM     users                                AS user
JOIN user_permission                        AS perm
ON (user.id = perm.user_id)
WHERE    user.email = :email
GROUP BY user.id;

-- name: create-user!
-- creates a new user record
INSERT INTO users (
  username,
  email,
  password)
VALUES (:username,
        :email,
        :password);

-- name: delete-user!
-- delete a user given the id
DELETE FROM users
WHERE       id = :id


-- name: insert-permission<!
-- inserts a permission type into the permission table
INSERT INTO permission (permission)
VALUES      (:permission);