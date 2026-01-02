package controller;

import database.CafeDAO;
import database.ReviewDAO;
import database.UserDAO;
import model.Cafe;
import model.Review;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ReviewsViewController {

    @FXML private TableView<Review> reviewsTable;
    @FXML private TableColumn<Review, Integer> idCol;
    @FXML private TableColumn<Review, String> cafeCol;
    @FXML private TableColumn<Review, String> userCol;
    @FXML private TableColumn<Review, Integer> ratingCol;
    @FXML private TableColumn<Review, String> commentCol;
    @FXML private TableColumn<Review, String> dateCol;

    @FXML private ComboBox<String> cafeFilterCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    private ReviewDAO reviewDAO = new ReviewDAO();
    private CafeDAO cafeDAO = new CafeDAO();
    private UserDAO userDAO = new UserDAO();

    private ObservableList<Review> reviewList = FXCollections.observableArrayList();
    private ObservableList<Review> originalList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // TABLE BINDINGS
        idCol.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());

        cafeCol.setCellValueFactory(cell -> {
            var cafe = cafeDAO.getCafeById(cell.getValue().getCafeId());
            return new SimpleStringProperty(cafe != null ? cafe.getName() : "Unknown");
        });

        userCol.setCellValueFactory(cell -> {
            var user = userDAO.getUserById(cell.getValue().getUserId());
            return new SimpleStringProperty(user != null ? user.getUsername() : "Unknown");
        });

        ratingCol.setCellValueFactory(cell -> cell.getValue().ratingProperty().asObject());
        commentCol.setCellValueFactory(cell -> cell.getValue().commentProperty());

        dateCol.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
        );

        // LOAD DATA
        originalList.setAll(reviewDAO.getAllReviews());
        reviewList.setAll(originalList);

        reviewsTable.setItems(reviewList);

        // LOAD CAFES FOR FILTER
        cafeFilterCombo.getItems().add("All Cafes");
        for (Cafe c : cafeDAO.getAllCafes()) {
            cafeFilterCombo.getItems().add(c.getName());
        }
        cafeFilterCombo.getSelectionModel().select("All Cafes");
    }

    // DELETE REVIEW BUTTON
    @FXML
    private void handleDeleteReview() {
        Review selected = reviewsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "No review selected.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete review ID " + selected.getId() + "?");
        confirm.setHeaderText("Confirm Delete Review");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK)
            return;

        boolean success = reviewDAO.deleteReviewAsAdmin(selected.getId());

        if (success) {
            reviewList.remove(selected);
            originalList.remove(selected);
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to delete review.").showAndWait();
        }
    }

    // FILTER LOGIC
    @FXML
    private void handleFilter() {

        String cafeName = cafeFilterCombo.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        reviewList.setAll(originalList);

        reviewList.setAll(
                reviewList.stream()
                        .filter(r -> {
                            // Filter cafe
                            if (!cafeName.equals("All Cafes")) {
                                Cafe c = cafeDAO.getCafeById(r.getCafeId());
                                if (c == null || !c.getName().equals(cafeName))
                                    return false;
                            }

                            // Filter start date
                            if (startDate != null) {
                                if (r.getCreatedAt().toLocalDate().isBefore(startDate))
                                    return false;
                            }

                            // Filter end date
                            if (endDate != null) {
                                if (r.getCreatedAt().toLocalDate().isAfter(endDate))
                                    return false;
                            }

                            return true;
                        })
                        .collect(Collectors.toList())
        );
    }

    // RESET FILTER
    @FXML
    private void handleResetFilter() {
        cafeFilterCombo.getSelectionModel().select("All Cafes");
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);

        reviewList.setAll(originalList);
    }
    
    //tombol back admin di review page
    @FXML
private void handleBack() {
    try {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/com/kelompok7/cafereviewapp/admin-dashboard.fxml")
        );

        javafx.scene.Parent root = loader.load();

        // ambil stage saat ini
        javafx.stage.Stage stage = (javafx.stage.Stage) reviewsTable.getScene().getWindow();

        stage.setScene(new javafx.scene.Scene(root));
        stage.setTitle("Admin Dashboard");
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
        new Alert(Alert.AlertType.ERROR, "Failed to open admin dashboard.").showAndWait();
    }
}
}
