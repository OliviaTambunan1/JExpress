
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
}