module com.example.cards {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.cards to javafx.fxml;
    exports com.example.cards;
}