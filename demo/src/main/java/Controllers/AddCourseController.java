package Controllers;

import Models.Course;
import Services.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCourseController {

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField semesterField;
    @FXML private TextField sectionField;
    @FXML private TextField studentsField;
    @FXML private TextField meetingField;
    @FXML private TextField roomField;
    @FXML private TextField averageField;
    @FXML private Label errorLabel;

    @FXML
    private void handleCreateCourse(ActionEvent event) {
        try {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String semester = semesterField.getText().trim();
            String section = sectionField.getText().trim();
            String meeting = meetingField.getText().trim();
            String room = roomField.getText().trim();
            String average = averageField.getText().trim();

            if (code.isEmpty() || name.isEmpty() || semester.isEmpty() || section.isEmpty()
                    || studentsField.getText().trim().isEmpty() || meeting.isEmpty() || room.isEmpty()) {
                errorLabel.setText("Please fill in all required fields.");
                return;
            }

            int students = Integer.parseInt(studentsField.getText().trim());
            //average nullified without grade input
            //String average = "0";

            Course course = new Course(code, name, semester, section, students, meeting, room, average);
            course.updateClassAverage();

            AppState.addCourse(course);

            goToDashboard(event);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Students must be a number.");
        } catch (Exception ex) {
            errorLabel.setText("Could not create course.");
            ex.printStackTrace();
        }
    }

    @FXML
    private void goToDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/ProfessorDashboardPage.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
