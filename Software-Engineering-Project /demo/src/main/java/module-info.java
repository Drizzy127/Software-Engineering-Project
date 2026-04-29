module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.auth.oauth2;
    //requires com.google.cloud.firestore;
    requires java.net.http;
    requires org.controlsfx.controls;

    exports Views;
    opens Views to javafx.fxml;

    exports Controllers;
    opens Controllers to javafx.fxml;
}