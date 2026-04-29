package Controllers;

import Models.Course;
import Models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class StudentViewController {
    @FXML private Label courseTitle;
    @FXML private Label meetingLabel;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> idColumn;
    @FXML private TableColumn<Student, String> gradeColumn;
    @FXML private TableColumn<Student, String> statusColumn;

    private final ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        idColumn.setCellValueFactory(data -> data.getValue().studentIdProperty());
        gradeColumn.setCellValueFactory(data -> data.getValue().gradeProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        studentTable.setItems(students);
    }

    public void setCourse(Course course) {
        if (course == null) return;
        courseTitle.setText(course.getCourseCode() + " - " + course.getCourseName());
        meetingLabel.setText("Meeting Time: Mon/Wed 1:30 PM");
        students.clear();

        switch (course.getCourseCode()) {
            case "CSC 101" -> students.addAll(
                    new Student("John Smith", "102938", "89", "Active"),
                    new Student("Avery Johnson", "104830", "90", "Active"),
                    new Student("David Fyffe", "106477", "79", "Active")
            );
            case "CSC 171" -> students.addAll(
                    new Student("Maria Lopez", "107552", "96", "Active"),
                    new Student("Kevin Patel", "108220", "91", "Active"),
                    new Student("Sam Rivera", "108991", "88", "Active")
            );
            default -> students.addAll(
                    new Student("Grace Lee", "109201", "94", "Active"),
                    new Student("Henry Adams", "109888", "86", "Active"),
                    new Student("Isabella Cruz", "110302", "92", "Active")
            );
        }
    }

    @FXML
    private void goToDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/ProfessorDashboardPage.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Professor Dashboard");
        stage.show();
    }
}
