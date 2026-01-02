package controller;

import database.UserDAO;
import model.AppUser;
import util.LoggerUtil;
import util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import util.WindowUtil;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
 

    private Stage stage;
        
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
private void goToRegister() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/secondary.fxml"));
        Scene scene = new Scene(loader.load());

        // ⬇️ Tambahan penting
        RegisterController rc = loader.getController();
        rc.setStage(stage);

        stage.setScene(scene);
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both username and password.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        AppUser user = userDAO.authenticate(username, password);
        
        if (user != null) {
            SessionManager.login(user);
            LoggerUtil.log("User logged in: " + user.getUsername() + " (" + user.getRole() + ")");
        }

        if (user != null) {
            try {
                FXMLLoader loader;
                if ("admin".equals(user.getRole())) {
                    loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/admin-dashboard.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("/com/kelompok7/cafereviewapp/user-dashboard.fxml"));
                }

                Scene scene = new Scene(loader.load());

// FIX: Ambil stage dari field username (bisa juga password atau tombol login)
Stage stage = (Stage) usernameField.getScene().getWindow();

stage.setScene(scene);
WindowUtil.applyDefaultSize(stage);
stage.show();
                
                stage.setWidth(1000);
                stage.setHeight(600);
                stage.setResizable(true);

                stage.setTitle("Cafe Review App - " + user.getRole().substring(0,1).toUpperCase() + user.getRole().substring(1) + " Dashboard");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load dashboard.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}