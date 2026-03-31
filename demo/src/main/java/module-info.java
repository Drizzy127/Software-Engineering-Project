module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    exports Views;
    opens Views to javafx.fxml;

    exports Controllers;
    opens Controllers to javafx.fxml;
}