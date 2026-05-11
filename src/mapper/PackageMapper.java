
package mapper;

import db.DBConnection;
import model.*;
import model.Package;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageMapper {

    public void createTableIfNotExists() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS packages (
                    id            VARCHAR(50)  PRIMARY KEY,
                    customer_id   INTEGER      NOT NULL REFERENCES customers(id),
                    sender_name   VARCHAR(100) NOT NULL,
                    receiver_name VARCHAR(100) NOT NULL,
                    destination   VARCHAR(100) NOT NULL,
                    weight_kg     NUMERIC(8,2) NOT NULL,
                    package_type  VARCHAR(20)  NOT NULL CHECK (package_type IN ('REGULAR','EXPRESS','FRAGILE')),
                    status        VARCHAR(20)  NOT NULL DEFAULT 'PENDING'
                );
                """;
        try (Statement stmt = DBConnection.getInstance().createStatement()) {
            stmt.execute(sql);
        }
    }



public void insert(Package pkg, int customerId) throws SQLException {
        String sql = """
                INSERT INTO packages (id, customer_id, sender_name, receiver_name,
                    destination, weight_kg, package_type, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setString(1, pkg.getId());
            stmt.setInt(2, customerId);
            stmt.setString(3, pkg.getSenderName());
            stmt.setString(4, pkg.getReceiverName());
            stmt.setString(5, pkg.getDestination());
            stmt.setDouble(6, pkg.getWeightKg());
            stmt.setString(7, pkg.getPackageType());
            stmt.setString(8, pkg.getStatus().name());
            stmt.executeUpdate();
        }
    
    
    }

public List<Package> findAll() throws SQLException {
        List<Package> list = new ArrayList<>();
        String sql = "SELECT * FROM packages ORDER BY id";
        try (Statement stmt = DBConnection.getInstance().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Package findById(String id) throws SQLException {
        String sql = "SELECT * FROM packages WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

}