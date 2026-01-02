module com.kelompok7.cafereviewapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.kelompok7.cafereviewapp to javafx.fxml;
    exports com.kelompok7.cafereviewapp;
}
