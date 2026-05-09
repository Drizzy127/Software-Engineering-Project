package Controllers;

import Models.Course;
import Models.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CourseViewController {

    @FXML private Label courseNameLabel;
    @FXML private Label semesterLabel;
    @FXML private Label sectionLabel;
    @FXML private Label studentsLabel;
    @FXML private Label timeLabel;
    @FXML private Label roomLabel;
    @FXML private Label avgLabel;
    @FXML private VBox courseContainer;

    @FXML private TextField quizWeightField;
    @FXML private TextField testWeightField;
    @FXML private TextField assignmentWeightField;
    @FXML private TextField participationWeightField;

    private Course course;

    @FXML
    public void initialize() {

    }

   public void setCourse(Course course) {
        this.course = course;

        if (course == null) {
            System.out.println("Course is NULL");
            return;
        }

        courseNameLabel.setText(course.getCourseName());
        semesterLabel.setText(course.getSemester());
        sectionLabel.setText(course.getSection());
        studentsLabel.setText("Students: " + course.getStudentCount());
        timeLabel.setText(course.getMeetingTime());
        roomLabel.setText(course.getRoom());
        avgLabel.setText("Class Avg: " + course.getClassAverage());

        showRoster(course);
        refreshCourseView();
    }


    @FXML
    private void applyWeights() {
        try {
            course.setQuizWeight(Double.parseDouble(quizWeightField.getText()));
            course.setTestWeight(Double.parseDouble(testWeightField.getText()));
            course.setAssignmentWeight(Double.parseDouble(assignmentWeightField.getText()));
            course.setParticipationWeight(Double.parseDouble(participationWeightField.getText()));

            setCourse(course);

        } catch (NumberFormatException e) {
            System.out.println("Invalid weight input");
        }
    }

    private void showRoster(Course course) {

        courseContainer.getChildren().clear();

        for (Student student : course.getStudents()) {

            HBox row = new HBox(15);

            row.setStyle("-fx-padding: 10; -fx-background-color: white;");
            row.setPickOnBounds(true);

            Label nameLabel = new Label(
                    student.getFirstName() + " " + student.getLastName()
            );


            Label avgLabel = new Label(
                    "Avg: " + String.format("%.1f", student.getRawAverage())
            );

            row.setOnMouseClicked(e -> openStudentView(student));
            nameLabel.setOnMouseClicked(e -> openStudentView(student));
            avgLabel.setOnMouseClicked(e -> openStudentView(student));

            row.getChildren().addAll(nameLabel, avgLabel);
            courseContainer.getChildren().add(row);
        }
    }

    private void openStudentView(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Pages/StudentViewPage.fxml")
            );

            Scene scene = new Scene(loader.load(), 900, 600);

            StudentViewController controller = loader.getController();
            controller.setStudent(student);
            controller.setCourse(course);


            controller.setOnSave(() -> refreshCourseView());

            Stage stage = (Stage) courseNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Student Profile");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void refreshCourseView() {

        course.updateClassAverage();

        double avg = 0;

        try {
            avg = Double.parseDouble(String.valueOf(course.getClassAverage()));
        } catch (Exception e) {
            avg = 0;
        }

        avgLabel.setText("Class Avg: " + String.format("%.2f", avg));
        studentsLabel.setText("Students: " + course.getStudentCount());

        showRoster(course);

        if (onCourseUpdate != null) {
            onCourseUpdate.run();
        }
    }

    @FXML
    private void goBackMouse(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/ProfessorDashboardPage.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 800);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private Runnable onCourseUpdate;

    public void setOnCourseUpdate(Runnable onCourseUpdate) {
        this.onCourseUpdate = onCourseUpdate;
    }
}