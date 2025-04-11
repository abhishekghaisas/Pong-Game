-- Step 1: Create a new database
CREATE DATABASE IF NOT EXISTS pongdb;

-- Step 2: Use the newly created database
USE pongdb;

-- Step 3: Create the 'users' table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,   -- Unique identifier (Primary Key)
    userName VARCHAR(255) NOT NULL,           -- User's username
    email VARCHAR(255) NOT NULL UNIQUE,       -- User's email (must be unique)
    password VARCHAR(255) NOT NULL           -- User's password
);

--       Create the 'scores' table
CREATE TABLE IF NOT EXISTS scores (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    score INT NOT NULL,
    date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    opponent_score INT,
    score_points INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Step 4: Create a new user (replace 'username' and 'password' with desired values)
-- NOTE: This creates the user for MySQL server access, not for the 'user' table
DROP USER IF EXISTS 'pong-user'@'localhost';

CREATE USER 'pong-user'@'localhost' IDENTIFIED BY 'pingpong24';

-- Step 5: Grant privileges to the new user (allow them to access and manipulate 'mydb')
GRANT ALL PRIVILEGES ON pongdb.* TO 'pong-user'@'localhost';

-- Step 6: Apply the changes (flush privileges)
FLUSH PRIVILEGES;

-- Step 7: Optional - Insert sample data into the 'users' table (can be customized)
INSERT INTO users (userName, email, password)
VALUES
    ('emily_freeman', 'emily.freeman@gmail.com', 'password123'),
    ('jane_smith', 'jane.smith@gmail.com', 'password456'),
    ('john_jones', 'john.jones@gmail.com', 'password789'),
    ('rita_dev', 'rita.dev@gmail.com', 'test789'),
    ('anusha_snow', 'anusha.naik@gmail.com', 'test3569');

-- Step 8: Optional - Insert sample data into the 'scores' table (can be customized)
INSERT INTO scores (user_id, score, opponent_score, score_points)
VALUES
    (1, 5, 3, 5),    
    (2, 11, 9, 11),
    (3, 21, 19, 21),
    (4, 5, 4, 5),
    (5, 11, 10, 11);
