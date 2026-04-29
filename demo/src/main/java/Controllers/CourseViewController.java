package Controllers;

import Models.Course;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseViewController {

    @FXML
    private Label courseNameLabel;
    @FXML
    private Label semesterLabel;
    @FXML
    private Label sectionLabel;
    @FXML
    private Label studentsLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label roomLabel;
    @FXML
    private Label avgLabel;

    private Course course;

    @FXML
    public void initialize() {

        // keep empty unless needed
    }

    // entry
    public void setCourse(Course course) {
        this.course = course;

        if (course == null) {
            System.out.println("Course is NULL");
            return;
        }

        System.out.println("Course loaded: " + course.getCourseName());

        if (courseNameLabel == null) {
            System.out.println("FXML not wired properly (labels are null)");
            return;
        }

        courseNameLabel.setText(course.getCourseName());
        semesterLabel.setText(course.getSemester());
        sectionLabel.setText(course.getSection());
        studentsLabel.setText("Students: " + course.getStudentCount());
        timeLabel.setText(course.getMeetingTime());
        roomLabel.setText(course.getRoom());
        avgLabel.setText("Class Avg: " + course.getClassAverage());
    }

    // back button
    @FXML
    private void goBackMouse(javafx.event.ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/ProfessorDashboardPage.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }






}





