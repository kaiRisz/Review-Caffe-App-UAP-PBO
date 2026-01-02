package controller;

import database.CafeDAO;
import model.Cafe;
import util.LoggerUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CafeFormController {
    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextArea descArea;

    private Cafe cafe;
    private Runnable onSaveCallback;
    private CafeDAO cafeDAO = new CafeDAO();

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
        if (cafe != null) {
            nameField.setText(cafe.getName());
            locationField.setText(cafe.getLocation());
            descArea.setText(cafe.getDescription());
        }
    }

    public void setOnSave(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String desc = descArea.getText().trim();

        if (name.isEmpty() || location.isEmpty()) {
            showError("Name and location are required.");
            return;
        }

        if (cafe == null) {
            if (cafeDAO.createCafe(name, location, desc)) {
            LoggerUtil.log("Admin created cafe: " + name);
            notifySaved();
            }
        } else {
        if (cafeDAO.updateCafe(cafe.getId(), name, location, desc)) {
            LoggerUtil.log("Admin updated cafe ID " + cafe.getId() + ": " + name);
            notifySaved();
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
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}