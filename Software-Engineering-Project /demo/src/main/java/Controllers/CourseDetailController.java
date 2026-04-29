package Controllers;

import Models.Course;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CourseDetailController {
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label meetingLabel;
    @FXML private Label roomLabel;
    @FXML private Label semesterLabel;

    private Course course;

    public void setCourseData(String courseCode, String courseName, String semester, String section,
                              String meetingTime, String room, String description) {
        this.course = new Course(courseCode, courseName);
        titleLabel.setText(courseCode + " - " + courseName);
        descriptionLabel.setText(description);
        meetingLabel.setText(meetingTime);
        roomLabel.setText("Room: " + room);
        semesterLabel.setText(semester + " | " + section);
    }

    @FXML
    private void openStudents(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/StudentViewPage.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);

        StudentViewController controller = loader.getController();
        controller.setCourse(course);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Students");
        stage.show();
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
