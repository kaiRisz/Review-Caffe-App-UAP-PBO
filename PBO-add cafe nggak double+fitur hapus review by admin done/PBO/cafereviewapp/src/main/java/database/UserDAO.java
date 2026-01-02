package database;

import model.Admin;
import model.AppUser;
import model.RegularUser;

import java.sql.*;

public class UserDAO {

    public UserDAO() {}

    public AppUser authenticate(String username, String password) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");

                if ("admin".equals(role)) {
                    return new Admin(id, username, password);
                } else {
                    return new RegularUser(id, username, password);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Login error: " + e.getMessage());
        }

        return null;
    }

    public boolean register(String username, String password) {
        String check = "SELECT username FROM users WHERE username = ?";
        String insert = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(check);
            PreparedStatement insertStmt = conn.prepareStatement(insert)
        ) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) return false;

            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, "user");
            insertStmt.executeUpdate();

            return true;

        } catch (SQLException e) {
    System.out.println("❌ Register error: " + e.getMessage());

    // Deteksi duplicate username
    if (e.getMessage().toLowerCase().contains("duplicate") ||
        e.getMessage().toLowerCase().contains("unique")) {
        return false;
    }

    // Kalau role salah, kasih pesan berbeda
    if (e.getMessage().toLowerCase().contains("role")) {
        throw new RuntimeException("Invalid role value in database schema!");
    }

    return false;
}
    }
    
    public AppUser getUserById(int userId) {
    String sql = "SELECT id, username, password, role FROM users WHERE id = ?";

    try (
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String role = rs.getString("role");

            if ("admin".equals(role)) {
                return new Admin(id, username, password);
            } else {
                return new RegularUser(id, username, password);
            }
        }
    } catch (SQLException e) {
        System.err.println("❌ Error fetching user: " + e.getMessage());
    }

    return null;
}
}