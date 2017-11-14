package com.mysite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GuestbookDatabaseConnection {

    private static Connection connection = null;

    static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:data/guestbook");
        }

        return connection;
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

}