package controller;


import database.CafeDAO;
import model.Cafe;
import util.SessionManager;
import util.LoggerUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    @FXML private TableView<Cafe> cafesTable;
    @FXML private TableColumn<Cafe, Integer> idColumn;
    @FXML private TableColumn<Cafe, String> nameColumn;
    @FXML private TableColumn<Cafe, String> locationColumn;
    @FXML private TableColumn<Cafe, String> descriptionColumn;
    @FXML private Button editCafeButton;
    @FXML private Button deleteCafeButton;

    private CafeDAO cafeDAO;
    private ObservableList<Cafe> cafeList;

    @FXML
    public void initialize() {
        cafeDAO = new CafeDAO();
        cafeList = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        locationColumn.setCellValueFactory(cell -> cell.getValue().locationProperty());
        descriptionColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());

        loadCafes();

        cafesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newSelection) -> {
            boolean enabled = (newSelection != null);
            editCafeButton.setDisable(!enabled);
            deleteCafeButton.setDisable(!enabled);
        });
    }

    private void loadCafes() {
        cafeList.setAll(cafeDAO.getAllCafes());
        cafesTable.setItems(cafeList);
    }

    @FXML
    private void handleAddCafe() {
        showCafeForm(null);
    }

    @FXML
    private void handleEditCafe() {
        Cafe selected = cafesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showCafeForm(selected);
        }
    }

    private void showCafeForm(Cafe cafe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/cafe-form.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(cafe == null ? "Add Cafe" : "Edit Cafe");

            CafeFormController controller = loader.getController();
            controller.setCafe(cafe);
            controller.setOnSave(() -> {
                stage.close();
                loadCafes(); // refresh table
            });

            stage.showAndWait();
        } catch (IOException e) {
            showError("Failed to open cafe form.");
        }
    }

    @FXML
    private void handleDeleteCafe() {
        Cafe selected = cafesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete cafe '" + selected.getName() + "'? Reviews will also be deleted.");
            confirm.setHeaderText("Confirm Deletion");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                if (cafeDAO.deleteCafe(selected.getId())) {
                    LoggerUtil.log("Admin deleted cafe ID " + selected.getId() + ": " + selected.getName());
                    loadCafes();
                } else {
                    showError("Failed to delete cafe.");
                }
            }
        }
    }

    @FXML
    private void handleViewReviews() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/reviews-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("All Reviews");
            stage.show();
        } catch (IOException e) {
            showError("Failed to open reviews.");
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}