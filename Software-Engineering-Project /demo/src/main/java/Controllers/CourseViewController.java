package Controllers;

import Models.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CourseViewController {

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<Course, String> courseCodeColumn;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    private ObservableList<Course> courseList;

    @FXML
    public void initialize() {

        courseCodeColumn.setCellValueFactory(data -> data.getValue().courseCodeProperty());
        courseNameColumn.setCellValueFactory(data -> data.getValue().courseNameProperty());

        courseList = FXCollections.observableArrayList(
                new Course("CSC 101", "Intro to CS"),
                new Course("CSC 202", "Data Structures"),
                new Course("CSC 325", "Software Engineering")
        );

        courseTable.setItems(courseList);

        //  working on a click to open students, needs debugging
        courseTable.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    openStudentView(row.getItem());
                }
            });
            return row;
        });
    }

    private void openStudentView(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/StudentView.fxml"));
            Scene scene = new Scene(loader.load());

            StudentViewController controller = loader.getController();
            controller.setCourse(course);

            Stage stage = (Stage) courseTable.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}