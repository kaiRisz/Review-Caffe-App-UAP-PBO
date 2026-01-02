package database;

import model.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private Connection connection;

    public ReviewDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean createReview(int cafeId, int userId, int rating, String comment) {
        String sql = "INSERT INTO reviews (cafe_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cafeId);
            stmt.setInt(2, userId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Failed to create review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT id, cafe_id, user_id, rating, comment, created_at FROM reviews ORDER BY created_at DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Review review = new Review(
                    rs.getInt("id"),
                    rs.getInt("cafe_id"),
                    rs.getInt("user_id"),
                    rs.getInt("rating"),
                    rs.getString("comment"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch reviews: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }

    public List<Review> getReviewsByCafeId(int cafeId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT id, cafe_id, user_id, rating, comment, created_at FROM reviews WHERE cafe_id = ? ORDER BY created_at DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cafeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Review review = new Review(
                    rs.getInt("id"),
                    rs.getInt("cafe_id"),
                    rs.getInt("user_id"),
                    rs.getInt("rating"),
                    rs.getString("comment"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch reviews for cafe: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }

    // UPDATE: Edit own review
    public boolean updateReview(int reviewId, int userId, int rating, String comment) {
        // Only allow update if the review belongs to this user
        String sql = "UPDATE reviews SET rating = ?, comment = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rating);
            stmt.setString(2, comment);
            stmt.setInt(3, reviewId);
            stmt.setInt(4, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Failed to update review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReview(int reviewId, int userId) {
        String sql = "DELETE FROM reviews WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Failed to delete review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReviewAsAdmin(int reviewId) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Admin failed to delete review: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}