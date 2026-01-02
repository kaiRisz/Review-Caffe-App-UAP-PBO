package controller;

import database.CafeDAO;
import database.ReviewDAO;
import database.UserDAO;
import model.Cafe;
import model.Review;
import util.SessionManager;
import util.LoggerUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.time.format.DateTimeFormatter;

public class UserDashboardController {

    // Cafes tab
    @FXML private TableView<Cafe> cafesTable;
    @FXML private TableColumn<Cafe, Integer> cafeIdCol;
    @FXML private TableColumn<Cafe, String> cafeNameCol;
    @FXML private TableColumn<Cafe, String> cafeLocationCol;
    @FXML private TableColumn<Cafe, String> cafeDescCol;

    // Reviews tab
    @FXML private TableView<Review> reviewsTable;
    @FXML private TableColumn<Review, Integer> reviewIdCol;
    @FXML private TableColumn<Review, String> reviewCafeCol;
    @FXML private TableColumn<Review, String> reviewUserCol;
    @FXML private TableColumn<Review, Integer> reviewRatingCol;
    @FXML private TableColumn<Review, String> reviewCommentCol;
    @FXML private TableColumn<Review, String> reviewDateCol;
    @FXML private Button editReviewButton;
    @FXML private Button deleteReviewButton;

    private CafeDAO cafeDAO = new CafeDAO();
    private ReviewDAO reviewDAO = new ReviewDAO();
    private UserDAO userDAO = new UserDAO();
    private ObservableList<Cafe> cafeList = FXCollections.observableArrayList();
    private ObservableList<Review> reviewList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cafeIdCol.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        cafeNameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());
        cafeLocationCol.setCellValueFactory(cell -> cell.getValue().locationProperty());
        cafeDescCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        loadCafes();

        reviewIdCol.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        reviewCafeCol.setCellValueFactory(cell -> {
            Cafe cafe = cafeDAO.getCafeById(cell.getValue().getCafeId());
            return cafe != null ? cafe.nameProperty() : new SimpleStringProperty("Unknown");
        });
        reviewUserCol.setCellValueFactory(cell -> {
            var user = userDAO.getUserById(cell.getValue().getUserId());
            return user != null ? new SimpleStringProperty(user.getUsername()) : new SimpleStringProperty("Unknown");
        });
        reviewRatingCol.setCellValueFactory(cell -> cell.getValue().ratingProperty().asObject());
        reviewCommentCol.setCellValueFactory(cell -> cell.getValue().commentProperty());
        reviewDateCol.setCellValueFactory(cell ->
            new SimpleStringProperty(
                cell.getValue().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            )
        );
        loadReviews();

        reviewsTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newSel) -> {
            boolean enabled = newSel != null && isOwnReview(newSel);
            editReviewButton.setDisable(!enabled);
            deleteReviewButton.setDisable(!enabled);
        });
    }

    private boolean isOwnReview(Review review) {
        return SessionManager.getCurrentUser().getId() == review.getUserId();
    }

    private void loadCafes() {
        cafeList.setAll(cafeDAO.getAllCafes());
        cafesTable.setItems(cafeList);
    }

    private void loadReviews() {
        reviewList.setAll(reviewDAO.getAllReviews());
        reviewsTable.setItems(reviewList);
    }

    @FXML
    private void handleWriteReview() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/review-form.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Write Review");

        ReviewFormController controller = loader.getController();
        controller.setOnSave(() -> {
            stage.close();
            loadReviews(); // refresh table
        });

        stage.showAndWait();
    } catch (IOException e) {
        new Alert(AlertType.ERROR, "Failed to open review form.").showAndWait();
    }
}

    @FXML
private void handleEditReview() {
    Review selected = reviewsTable.getSelectionModel().getSelectedItem();
    if (selected != null && isOwnReview(selected)) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/review-form.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Review");

            ReviewFormController controller = loader.getController();
            controller.setReview(selected);
            controller.setOnSave(() -> {
                stage.close();
                loadReviews();
            });

            stage.showAndWait();
        } catch (IOException e) {
            new Alert(AlertType.ERROR, "Failed to open edit form.").showAndWait();
        }
    } else {
        new Alert(AlertType.WARNING, "You can only edit your own reviews.").showAndWait();
    }
}

    @FXML
    private void handleDeleteReview() {
        Review selected = reviewsTable.getSelectionModel().getSelectedItem();
        if (selected != null && isOwnReview(selected)) {
            Alert confirm = new Alert(AlertType.CONFIRMATION,
                "Are you sure you want to delete your review?\n\n\"" + selected.getComment() + "\"");
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Review");
            
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (reviewDAO.deleteReview(selected.getId(), selected.getUserId())) {
                    LoggerUtil.log("User deleted own review ID " + selected.getId());
                    loadReviews();
                } else {
                    new Alert(AlertType.ERROR, "Failed to delete review. Please try again.").showAndWait();
                }
            }
        } else {
            new Alert(AlertType.WARNING, "You can only delete your own reviews.").showAndWait();
        }
    }
}