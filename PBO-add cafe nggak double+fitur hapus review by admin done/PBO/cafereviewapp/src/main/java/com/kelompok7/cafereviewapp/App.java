package com.kelompok7.cafereviewapp;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
public void start(Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/kelompok7/cafereviewapp/primary.fxml"));
    Scene scene = new Scene(loader.load());

    LoginController controller = loader.getController();
    controller.setStage(stage);

    stage.setTitle("Cafe Review App - Login");
    stage.setWidth(1000);
    stage.setHeight(600);
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
}

    public static void main(String[] args) {
        launch();
}
}
