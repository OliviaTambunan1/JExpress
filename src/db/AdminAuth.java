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
}
