@echo off

set connection=127.0.0.1:3305
set user=root
set password=1234

set /p connection="Enter MYSQL Connection Database(default = 127.0.0.1:3305/se14): ":
set /p user="Enter User(default = root): ":
set /p password="Enter Password(default = 1234): ":

chcp 65001
echo package com.se14.repository.db_impl;> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo.>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo import java.sql.Connection;>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo import java.sql.DriverManager;>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo import java.sql.SQLException;>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo.>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo public class DatabaseConnection {>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo     private static final String URL = "jdbc:mysql://%connection%";>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo     private static final String USER = "%user%";>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo     private static final String PASSWORD = "%password%";>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo.>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo     public static Connection getConnection() throws SQLException {>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo         return DriverManager.getConnection(URL, USER, PASSWORD);>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo     }>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java
echo }>> src\main\java\com\se14\repository\db_impl\DatabaseConnection.java

exit /b 0