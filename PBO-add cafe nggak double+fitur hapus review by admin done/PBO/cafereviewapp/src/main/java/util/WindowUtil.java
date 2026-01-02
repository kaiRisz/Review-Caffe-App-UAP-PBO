package util;

import javafx.stage.Stage;

public class WindowUtil {

    public static final double DEFAULT_WIDTH = 1000;
    public static final double DEFAULT_HEIGHT = 600;

    public static void applyDefaultSize(Stage stage) {
        stage.setWidth(DEFAULT_WIDTH);
        stage.setHeight(DEFAULT_HEIGHT);
        stage.setResizable(false);
    }
}