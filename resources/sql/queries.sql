-- name: create-user!
-- creates a new user record
INSERT INTO users
(name, email, password)
VALUES (:name, :email, :password)

-- name: get-user
-- retrieve a user given the id.
SELECT id, name, email FROM users
WHERE id = :id

-- name: get-users
-- retrieve a user given the id.
SELECT id, name, email FROM users

-- name: delete-user!
-- delete a user given the id
DELETE FROM users
WHERE id = :id