package mapper;

import db.DBConnection;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {

    public void createTableIfNotExists() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS customers (
                    id    SERIAL PRIMARY KEY,
                    name  VARCHAR(100) NOT NULL,
                    phone VARCHAR(20)  NOT NULL
                );
                """;
        try (Statement stmt = DBConnection.getInstance().createStatement()) {
            stmt.execute(sql);
        }
    }
 public void insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (name, phone) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = DBConnection.getInstance().prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) customer.setId(rs.getInt("id"));
        }
    }
}