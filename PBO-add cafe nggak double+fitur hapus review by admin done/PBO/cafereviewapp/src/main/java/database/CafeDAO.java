package database;

import model.Cafe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CafeDAO {

    private Connection connection;

    public CafeDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean createCafe(String name, String location, String description) {
        String sql = "INSERT INTO cafes (name, location, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, description);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Failed to create cafe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Cafe> getAllCafes() {
    List<Cafe> cafes = new ArrayList<>();
    String sql = "SELECT id, name, location, description FROM cafes";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Cafe cafe = new Cafe(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("location"),
                rs.getString("description")
            );
            cafes.add(cafe);
        }
    } catch (SQLException e) {
        System.err.println("❌ Failed to fetch cafes: " + e.getMessage());
        e.printStackTrace();
    }
    return cafes;
}
    
    // Add this inside CafeDAO.java
    public Cafe getCafeById(int id) {
        String sql = "SELECT id, name, location, description FROM cafes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cafe(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getString("description")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch cafe by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCafe(int id, String name, String location, String description) {
        String sql = "UPDATE cafes SET name = ?, location = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, description);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Failed to update cafe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCafe(int id) {
        String sql = "DELETE FROM cafes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Failed to delete cafe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
}