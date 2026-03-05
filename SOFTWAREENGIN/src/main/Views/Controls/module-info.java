module org.example.softwareengin {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.softwareengin to javafx.fxml;
    exports org.example.softwareengin;
}