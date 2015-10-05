CREATE TABLE users
(id SERIAL PRIMARY KEY,
 name VARCHAR(150),
 email VARCHAR(40),
 admin BOOLEAN,
 last_login TIME,
 is_active BOOLEAN,
 password VARCHAR(100));
