package Controllers;

import Models.Course;
import Models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class StudentViewController {

    @FXML
    private Label courseTitle;

    @FXML
    private ListView<Student> studentList;

    private ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        studentList.setItems(students);
    }

    public void setCourse(Course course) {

        courseTitle.setText(course.getCourseCode() + " - " + course.getCourseName());

        students.clear();

        switch (course.getCourseCode()) {
            case "CSC 101":
                students.addAll(
                        new Student("Alice"),
                        new Student("Bob"),
                        new Student("Charlie")
                );
                break;

            case "CSC 202":
                students.addAll(
                        new Student("David"),
                        new Student("Emma"),
                        new Student("Frank")
                );
                break;

            case "CSC 325":
                students.addAll(
                        new Student("Grace"),
                        new Student("Henry"),
                        new Student("Isabella")
                );
                break;
        }
    }
}