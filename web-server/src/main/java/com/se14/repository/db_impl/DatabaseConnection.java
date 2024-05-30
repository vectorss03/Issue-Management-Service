package com.se14.repository.db_impl;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://172.30.1.54/se14";
    private static final String USER = "root";
    private static final String PASSWORD = "rkdshrua6428!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}