package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5555/cafereviewdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    private DatabaseConnection() {}

    public static Connection getConnection() {
    try {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(true); // <-- FIX PENTING
        return conn;
    } catch (SQLException e) {
        System.err.println("❌ Failed to connect:");
        e.printStackTrace();
        return null;
    }
}
}