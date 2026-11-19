package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/employment_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = System.getenv("EMPLOYMENT_DB_PASSWORD");

    private Database() { }

    public static Connection getDBConnection() throws SQLException {
        if (DB_PASSWORD == null) {
            throw new SQLException("Set EMPLOYMENT_DB_PASSWORD first.");
        }
        return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
    }
}
