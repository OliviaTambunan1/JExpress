
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
    public List<Package> findByCustomerId(int customerId) throws SQLException {
        List<Package> list = new ArrayList<>();
        String sql = "SELECT * FROM packages WHERE customer_id = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean updateStatus(String id, PackageStatus status) throws SQLException {
        String sql = "UPDATE packages SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setString(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public Map<String, List<Package>> findAllGroupedByCustomer() throws SQLException {
        Map<String, List<Package>> grouped = new HashMap<>();
        String sql = """
                SELECT p.*, c.name AS customer_name
                FROM packages p JOIN customers c ON p.customer_id = c.id
                ORDER BY c.name
                """;
        try (Statement stmt = DBConnection.getInstance().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String customerName = rs.getString("customer_name");
                grouped.computeIfAbsent(customerName, k -> new ArrayList<>())
                       .add(mapRow(rs));
            }
        }
        return grouped;
    }

    private Package mapRow(ResultSet rs) throws SQLException {
        String type     = rs.getString("package_type");
        String id       = rs.getString("id");
        String sender   = rs.getString("sender_name");
        String receiver = rs.getString("receiver_name");
        String dest     = rs.getString("destination");
        double weight   = rs.getDouble("weight_kg");
        PackageStatus status = PackageStatus.fromString(rs.getString("status"));

        Package pkg = switch (type) {
            case "EXPRESS" -> new ExpressPackage(id, sender, receiver, dest, weight);
            case "FRAGILE" -> new FragilePackage(id, sender, receiver, dest, weight);
            default        -> new RegularPackage(id, sender, receiver, dest, weight);
        };
        pkg.setStatus(status);
        return pkg;
    }

}