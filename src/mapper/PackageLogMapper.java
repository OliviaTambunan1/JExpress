
package mapper;

import db.DBConnection;
import model.PackageLog;
import model.PackageStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageLogMapper {

    public void createTableIfNotExists() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS package_logs (
                    id          SERIAL PRIMARY KEY,
                    package_id  VARCHAR(50)  NOT NULL REFERENCES packages(id),
                    status      VARCHAR(20)  NOT NULL,
                    location    VARCHAR(100) NOT NULL,
                    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
                );
                """;
        try (Statement stmt = DBConnection.getInstance().createStatement()) {
            stmt.execute(sql);
        }
    }

    public void insert(PackageLog log) throws SQLException {
        String sql = "INSERT INTO package_logs (package_id, status, location) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setString(1, log.getPackageId());
            stmt.setString(2, log.getStatus().name());
            stmt.setString(3, log.getLocation());
            stmt.executeUpdate();
        }
    }
}