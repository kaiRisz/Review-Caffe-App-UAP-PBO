package controller;

import database.CafeDAO;
import database.ReviewDAO;
import model.Cafe;
import model.Review;
import util.LoggerUtil;
import util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReviewFormController {

    @FXML private ComboBox<Cafe> cafeCombo;
    @FXML private ComboBox<Integer> ratingCombo;
    @FXML private TextArea commentArea;

    private ReviewDAO reviewDAO = new ReviewDAO();
    private CafeDAO cafeDAO = new CafeDAO();
    private Review reviewToEdit = null; // null = new review, otherwise = edit
    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        // Load cafes
        ObservableList<Cafe> cafes = FXCollections.observableArrayList(cafeDAO.getAllCafes());
        cafeCombo.setItems(cafes);

        // Load ratings (1 to 5)
        ObservableList<Integer> ratings = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        ratingCombo.setItems(ratings);

        if (reviewToEdit != null) {
            // Edit mode: pre-fill fields
            cafeCombo.setValue(getCafeById(reviewToEdit.getCafeId()));
            ratingCombo.setValue(reviewToEdit.getRating());
            commentArea.setText(reviewToEdit.getComment());
        }
    }

    private Cafe getCafeById(int id) {
        for (Cafe cafe : cafeCombo.getItems()) {
            if (cafe.getId() == id) return cafe;
        }
        return null;
    }

    public void setReview(Review review) {
        this.reviewToEdit = review;
    }

    public void setOnSave(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave() {
        Cafe selectedCafe = cafeCombo.getValue();
        Integer rating = ratingCombo.getValue();
        String comment = commentArea.getText().trim();

        if (selectedCafe == null || rating == null || comment.isEmpty()) {
            showError("Please select a cafe, rating, and enter a comment.");
            return;
        }

        int userId = SessionManager.getCurrentUser().getId();

        if (reviewToEdit == null) {
            // Create new review
            if (reviewDAO.createReview(selectedCafe.getId(), userId, rating, comment)) {
                LoggerUtil.log("User wrote new review for cafe ID " + selectedCafe.getId());
                notifySaved();
            } else {
                showError("Failed to save review.");
            }
        } else {
            // Update existing review
            if (reviewDAO.updateReview(reviewToEdit.getId(), userId, rating, comment)) {
                LoggerUtil.log("User updated review ID " + reviewToEdit.getId());
                notifySaved();
            } else {
                showError("Failed to update review.");
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void notifySaved() {
        if (onSaveCallback != null) onSaveCallback.run();
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cafeCombo.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}