#!/bin/bash

set -e

# Create userdb if it doesn't exist
mariadb -u root -p"$MYSQL_ROOT_PASSWORD" <<-EOSQL
    CREATE DATABASE IF NOT EXISTS userdb;
    GRANT ALL PRIVILEGES ON userdb.* TO 'root'@'%';
    FLUSH PRIVILEGES;
EOSQL

# Create map_platform if it doesn't exist
mariadb -u root -p"$MYSQL_ROOT_PASSWORD" <<-EOSQL
    CREATE DATABASE IF NOT EXISTS map_platform;
    GRANT ALL PRIVILEGES ON map_platform.* TO 'map_user'@'%';
    FLUSH PRIVILEGES;
EOSQL 