package controller;

import database.UserDAO;
import model.AppUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
private void handleRegister() {
    String username = usernameField.getText().trim();
    String pass = passwordField.getText();
    String confirm = confirmField.getText();

    if (username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Empty Field", "Please fill all fields!");
        return;
    }

    if (!pass.equals(confirm)) {
        showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match!");
        return;
    }

    UserDAO dao = new UserDAO();

    if (dao.register(username, pass)) {
        showAlert(Alert.AlertType.INFORMATION, "Success", "Registration complete!");
        goBackToLogin();
    } else {
        showAlert(Alert.AlertType.ERROR, "Error", "Username already exists!");
    }
}

    @FXML
    private void goBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/primary.fxml"));
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
private void handleBack() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/primary.fxml"));
        Scene scene = new Scene(loader.load());

        LoginController lc = loader.getController();
        lc.setStage(stage);

        stage.setScene(scene);
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}