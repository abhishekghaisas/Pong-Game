package com.pong.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {


    private static final String URL = "jdbc:mysql://localhost:3306/pongdb";
    private static final String USER = "pong-user";
    private static final String PASSWORD = "pingpong24";

    public static Connection getConnection() throws SQLException {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error connecting to the database.", e);
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}