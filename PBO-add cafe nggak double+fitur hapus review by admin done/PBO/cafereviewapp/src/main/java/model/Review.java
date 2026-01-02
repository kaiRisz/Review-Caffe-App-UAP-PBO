package model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Review {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty cafeId = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final IntegerProperty rating = new SimpleIntegerProperty();
    private final StringProperty comment = new SimpleStringProperty();
    private LocalDateTime createdAt;

    public Review() {}

    public Review(int id, int cafeId, int userId, int rating, String comment, LocalDateTime createdAt) {
        setId(id);
        setCafeId(cafeId);
        setUserId(userId);
        setRating(rating);
        setComment(comment);
        setCreatedAt(createdAt);
    }

    // Property getters (for TableView binding)
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty cafeIdProperty() { return cafeId; }
    public IntegerProperty userIdProperty() { return userId; }
    public IntegerProperty ratingProperty() { return rating; }
    public StringProperty commentProperty() { return comment; }

    public int getId() { return id.get(); }
    public int getCafeId() { return cafeId.get(); }
    public int getUserId() { return userId.get(); }
    public int getRating() { return rating.get(); }
    public String getComment() { return comment.get(); }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id.set(id); }
    public void setCafeId(int cafeId) { this.cafeId.set(cafeId); }
    public void setUserId(int userId) { this.userId.set(userId); }
    public void setRating(int rating) { this.rating.set(rating); }
    public void setComment(String comment) { this.comment.set(comment); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id.get() +
                ", cafeId=" + cafeId.get() +
                ", userId=" + userId.get() +
                ", rating=" + rating.get() +
                ", comment='" + comment.get() + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}