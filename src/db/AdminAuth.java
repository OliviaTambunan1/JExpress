package db;

import java.sql.*;

public class AdminAuth {

    public static void createTableIfNotExists() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS admins (
                    id       SERIAL PRIMARY KEY,
                    username VARCHAR(50)  NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL
                );
                """;
        try (Statement stmt = DBConnection.getInstance().createStatement()) {
            stmt.execute(sql);
        }
    }
     public static void register(String username, String password) throws SQLException {
        String sql = "INSERT INTO admins (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        }
    }
       public static boolean login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
      public static boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admins WHERE username = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

}
