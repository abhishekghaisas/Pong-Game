#!/bin/bash

# Variables
MYSQL_USER="root"              # MySQL username (typically 'root')
MYSQL_HOST="localhost"         # MySQL host (typically 'localhost')
MYSQL_PORT="3306"              # MySQL port (default is 3306)
SQL_SCRIPT="mysql-setup.sql"   # Path to the SQL script you want to run

# Prompt for password
read -sp "Enter MySQL root password: " MYSQL_PASSWORD
echo

# 3. Check if the SQL script exists
if [ ! -f "$SQL_SCRIPT" ]; then
    echo "SQL script $SQL_SCRIPT not found!"
    exit 1
fi

# 4. Run the SQL script
echo "Running SQL script..."
mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -h "$MYSQL_HOST" -P "$MYSQL_PORT" < "$SQL_SCRIPT"

# 5. Check if the script executed successfully
if [ $? -eq 0 ]; then
    echo "SQL script executed successfully!"
else
    echo "Error: SQL script execution failed!"
fi
