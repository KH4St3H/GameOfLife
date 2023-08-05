module com.example.gameofdeath {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gameofdeath to javafx.fxml;
    exports com.example.gameofdeath;
}