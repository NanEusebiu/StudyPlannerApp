package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String BASE_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_NAME = "login_system";
    private static final String URL = BASE_URL + "/" + DB_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = "root123";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(BASE_URL, USER, PASSWORD)) {
            conn.createStatement().execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("Failed to create database: " + e.getMessage());
            return false;
        }

        try (Connection conn = getConnection()) {
            // Create users table
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(50) UNIQUE NOT NULL, " +
                            "password VARCHAR(100) NOT NULL, " +
                            "full_name VARCHAR(100) NOT NULL, " +
                            "role VARCHAR(20) NOT NULL)"
            );

            // Create subjects table
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS subjects (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "user_id INT, " +
                            "FOREIGN KEY (user_id) REFERENCES users(id))"
            );

            // Create tasks table
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS tasks (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "title VARCHAR(100) NOT NULL, " +
                            "description TEXT, " +
                            "deadline DATE NOT NULL, " +
                            "status VARCHAR(20) NOT NULL, " +
                            "subject_id INT, " +
                            "user_id INT, " +
                            "FOREIGN KEY (subject_id) REFERENCES subjects(id), " +
                            "FOREIGN KEY (user_id) REFERENCES users(id))"
            );

            return true;
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            return false;
        }
    }

}