#!/bin/bash

# Default connection details
connection="127.0.0.1:3305/se14"
user="root"
password="1234"

# Ask user to enter details with defaults shown
read -p "Enter MYSQL Connection Database (default = 127.0.0.1:3305/se14): " input_connection
read -p "Enter User (default = root): " input_user
read -s -p "Enter Password (default = 1234): " input_password
echo

# Use default if no input provided
connection=${input_connection:-$connection}
user=${input_user:-$user}
password=${input_password:-$password}

# Create or overwrite Java file to setup database connection
echo "package com.se14.repository.db_impl;" > src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "import java.sql.Connection;" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "import java.sql.DriverManager;" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "import java.sql.SQLException;" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "public class DatabaseConnection {" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "    private static final String URL = \"jdbc:mysql://$connection\";" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "    private static final String USER = \"$user\";" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "    private static final String PASSWORD = \"$password\";" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "    public static Connection getConnection() throws SQLException {" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "        return DriverManager.getConnection(URL, USER, PASSWORD);" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "    }" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java
echo "}" >> src/main/java/com/se14/repository/db_impl/DatabaseConnection.java

